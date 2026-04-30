package com.musicleague.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Aldreyous Smith
 * Spring 2026
 * Software Engineering II
 * Mode 2 Backend - Guess The Song
 *  
 *   */
@Controller
public class WebController {
    
    @GetMapping("/")
    public String index() {
        return "forward:/deezer-game.html";
    }
}