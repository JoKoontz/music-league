package com.musicleague.controller;

import java.security.Principal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.musicleague.dto.SubmissionDto;
import com.musicleague.model.Round;
import com.musicleague.model.User;
import com.musicleague.service.RoundService;
import com.musicleague.service.SubmissionService;
import com.musicleague.service.UserService;

@Controller
@RequestMapping("/submissions")
public class SubmissionController {

    private final SubmissionService submissionService;
    private final RoundService roundService;
    private final UserService userService;

    public SubmissionController(SubmissionService submissionService,
                                RoundService roundService,
                                UserService userService) {
        this.submissionService = submissionService;
        this.roundService = roundService;
        this.userService = userService;
    }

    @GetMapping("/create/{roundId}")
    public String showSubmissionForm(@PathVariable Long roundId, Model model) {
        model.addAttribute("submissionDto", new SubmissionDto());
        model.addAttribute("roundId", roundId);
        return "create-submission";
    }

    @PostMapping("/create/{roundId}")
    public String submitSong(@PathVariable Long roundId,
                             @ModelAttribute SubmissionDto submissionDto,
                             Principal principal,
                             Model model) {
        try {
            Round round = roundService.getRoundById(roundId);
            roundService.updateRoundStatus(round);

            User user = userService.findByUsername(principal.getName()).orElseThrow();
            submissionService.submitSong(round, user, submissionDto);

            return "redirect:/rounds/" + roundId;
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("roundId", roundId);
            return "create-submission";
        }
    }
}