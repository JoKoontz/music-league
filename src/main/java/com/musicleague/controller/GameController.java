package com.musicleague.controller;

import com.musicleague.deezer.model.GameSession;
import com.musicleague.deezer.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Aldreyous Smith
 * Spring 2026
 * Software Engineering II
 * Mode 2 Backend - Guess The Song
 *  
 *   */

@RestController
@RequestMapping("/api/deezer")
@CrossOrigin(origins = "*")
public class GameController {
    
    @Autowired
    private GameService gameService;
    
    @PostMapping("/start")
    public Map<String, Object> startGame(@RequestBody Map<String, Object> request) {
        String playerName = (String) request.get("playerName");
        int totalRounds = (int) request.get("totalRounds");
        
        GameSession session = gameService.startNewGame(playerName, totalRounds);
        
        Map<String, Object> response = new HashMap<>();
        response.put("sessionCode", session.getSessionCode());
        response.put("artistName", session.getArtistName());
        response.put("previewUrl", session.getPreviewUrl());
        response.put("currentRound", session.getCurrentRound());
        response.put("totalRounds", session.getTotalRounds());
        response.put("gameComplete", false);
        
        return response;
    }
    
    @PostMapping("/guess")
    public Map<String, Object> submitGuess(@RequestBody Map<String, Object> request) {
        String sessionCode = (String) request.get("sessionCode");
        String guess = (String) request.get("guess");
        int points = (int) request.get("points");
        
        return gameService.processGuess(sessionCode, guess, points);
    }
    
    @PostMapping("/next-round")
    public Map<String, Object> nextRound(@RequestBody Map<String, Object> request) {
        String sessionCode = (String) request.get("sessionCode");
        
        GameSession session = gameService.nextRound(sessionCode);
        
        if (session == null || !session.getIsActive()) {
            Map<String, Object> response = new HashMap<>();
            response.put("gameComplete", true);
            response.put("finalScore", session != null ? session.getTotalScore() : 0);
            return response;
        }
        
        Map<String, Object> response = new HashMap<>();
        response.put("sessionCode", session.getSessionCode());
        response.put("artistName", session.getArtistName());
        response.put("previewUrl", session.getPreviewUrl());
        response.put("currentRound", session.getCurrentRound());
        response.put("totalRounds", session.getTotalRounds());
        response.put("gameComplete", false);
        
        return response;
    }
    
    @GetMapping("/test")
    public Map<String, String> test() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "Deezer Edition is running!");
        response.put("message", "Popular songs from Taylor Swift, Adele, The Weeknd, and more!");
        return response;
    }
}