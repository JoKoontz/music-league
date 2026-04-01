package com.musicleague.controller;

import java.security.Principal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.musicleague.dto.LeagueDto;
import com.musicleague.model.League;
import com.musicleague.model.User;
import com.musicleague.service.LeagueService;
import com.musicleague.service.UserService;

@Controller
@RequestMapping("/leagues")
public class LeagueController {

    private final LeagueService leagueService;
    private final UserService userService;

    public LeagueController(LeagueService leagueService, UserService userService) {
        this.leagueService = leagueService;
        this.userService = userService;
    }

    @GetMapping("/create")
    public String showCreateLeagueForm(Model model) {
        model.addAttribute("leagueDto", new LeagueDto());
        return "create-league";
    }

    @PostMapping("/create")
    public String createLeague(@ModelAttribute LeagueDto leagueDto, Principal principal) {
        User user = userService.findByUsername(principal.getName()).orElseThrow();
        League league = leagueService.createLeague(leagueDto, user);
        return "redirect:/leagues/" + league.getId();
    }

    @GetMapping("/{id}")
    public String viewLeague(@PathVariable Long id, Model model) {
        League league = leagueService.getLeagueById(id);
        model.addAttribute("league", league);
        model.addAttribute("members", leagueService.getLeagueMembers(league));
        return "league-details";
    }

    @GetMapping("/join")
    public String showJoinForm() {
        return "join-league";
    }

    @PostMapping("/join")
    public String joinLeague(@RequestParam String inviteCode, Principal principal, Model model) {
        try {
            User user = userService.findByUsername(principal.getName()).orElseThrow();
            leagueService.joinLeague(inviteCode, user);
            return "redirect:/dashboard";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return "join-league";
        }
    }
}