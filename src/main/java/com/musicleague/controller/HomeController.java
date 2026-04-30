package com.musicleague.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "index";
    }

    @GetMapping("/dashboard")
    public String dashboard() {
        return "dashboard";
    }
    
    @GetMapping("/guess-game")
    public String guessGame() {
        return "deezer-game";
    }

    /*
    @GetMapping("/guess-game")
    public String guessGame() {
        return "guess-game";
    } */
   
    /*
    @GetMapping("/deezer-game")
    public String deezerGame() {
        return "deezer-game"; 
    } */
}