package com.musicleague.controller;

import java.security.Principal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.musicleague.dto.VoteDto;
import com.musicleague.model.Round;
import com.musicleague.model.User;
import com.musicleague.service.RoundService;
import com.musicleague.service.SubmissionService;
import com.musicleague.service.UserService;
import com.musicleague.service.VoteService;

@Controller
@RequestMapping("/votes")
public class VoteController {

    private final VoteService voteService;
    private final RoundService roundService;
    private final SubmissionService submissionService;
    private final UserService userService;

    public VoteController(VoteService voteService,
                          RoundService roundService,
                          SubmissionService submissionService,
                          UserService userService) {
        this.voteService = voteService;
        this.roundService = roundService;
        this.submissionService = submissionService;
        this.userService = userService;
    }

    @GetMapping("/create/{roundId}")
    public String showVoteForm(@PathVariable Long roundId, Model model) {
        Round round = roundService.getRoundById(roundId);
        roundService.updateRoundStatus(round);

        model.addAttribute("round", round);
        model.addAttribute("submissions", submissionService.getSubmissionsForRound(round));
        model.addAttribute("voteDto", new VoteDto());
        return "vote-page";
    }

    @PostMapping("/create/{roundId}")
    public String castVote(@PathVariable Long roundId,
                           @ModelAttribute VoteDto voteDto,
                           Principal principal,
                           Model model) {
        try {
            Round round = roundService.getRoundById(roundId);
            roundService.updateRoundStatus(round);

            User user = userService.findByUsername(principal.getName()).orElseThrow();
            voteService.castVote(round, user, voteDto);

            return "redirect:/rounds/" + roundId;
        } catch (RuntimeException e) {
            Round round = roundService.getRoundById(roundId);
            model.addAttribute("round", round);
            model.addAttribute("submissions", submissionService.getSubmissionsForRound(round));
            model.addAttribute("error", e.getMessage());
            return "vote-page";
        }
    }
}