package com.musicleague.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.musicleague.dto.RoundDto;
import com.musicleague.model.League;
import com.musicleague.model.Round;
import com.musicleague.service.LeagueService;
import com.musicleague.service.RoundService;
import com.musicleague.service.SubmissionService;

@Controller
@RequestMapping("/rounds")
public class RoundController {

    private final RoundService roundService;
    private final LeagueService leagueService;
    private final SubmissionService submissionService;

    public RoundController(RoundService roundService,
                           LeagueService leagueService,
                           SubmissionService submissionService) {
        this.roundService = roundService;
        this.leagueService = leagueService;
        this.submissionService = submissionService;
    }

    @GetMapping("/create/{leagueId}")
    public String showCreateRoundForm(@PathVariable Long leagueId, Model model) {
        model.addAttribute("roundDto", new RoundDto());
        model.addAttribute("leagueId", leagueId);
        return "create-round";
    }

    @PostMapping("/create/{leagueId}")
    public String createRound(@PathVariable Long leagueId, @ModelAttribute RoundDto roundDto) {
        League league = leagueService.getLeagueById(leagueId);
        Round round = roundService.createRound(league, roundDto);
        return "redirect:/rounds/" + round.getId();
    }

    @GetMapping("/{id}")
    public String viewRound(@PathVariable Long id, Model model) {
        Round round = roundService.getRoundById(id);
        roundService.updateRoundStatus(round);

        model.addAttribute("round", round);
        model.addAttribute("submissions", submissionService.getSubmissionsForRound(round));
        return "round-details";
    }
}