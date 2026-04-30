package com.musicleague.service;

import com.musicleague.model.GameSession;
import com.musicleague.model.Song;
import com.musicleague.repository.GameSessionRepository;
import com.musicleague.repository.SongRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

/**
 * Aldreyous Smith
 * Spring 2026
 * Software Engineering II
 * Mode 2 Backend - Guess The Song
 *  
 *   */

@Service
public class GameService {
    
    @Autowired
    private GameSessionRepository gameSessionRepository;
    
    @Autowired
    private SongRepository songRepository;
    
    @Autowired
    private DeezerService deezerService;
    
    private Random random = new Random();
    private Map<String, GameSession> activeSessions = new HashMap<>();
    // Track which songs have been used in the current game
    private Map<String, java.util.Set<String>> usedSongsPerSession = new HashMap<>();
    
    public GameSession startNewGame(String playerName, int totalRounds) {
        Song randomSong = deezerService.getRandomSong();
        if (randomSong == null) return null;
        
        songRepository.save(randomSong);
        
        String sessionCode = generateSessionCode();
        GameSession session = new GameSession();
        session.setSessionCode(sessionCode);
        session.setPlayerName(playerName);
        session.setTotalRounds(totalRounds);
        session.setCurrentRound(1);
        session.setTotalScore(0);
        session.setCorrectGuesses(0);
        session.setIsActive(true);
        session.setStartTime(LocalDateTime.now());
        session.setCurrentSongId(randomSong.getSpotifyId());
        session.setCurrentSongName(randomSong.getName());
        session.setArtistName(randomSong.getArtistName());
        session.setPreviewUrl(randomSong.getPreviewUrl());
        
        gameSessionRepository.save(session);
        activeSessions.put(sessionCode, session);
        
        // Track used songs for this session
        usedSongsPerSession.put(sessionCode, new java.util.HashSet<>());
        usedSongsPerSession.get(sessionCode).add(randomSong.getSpotifyId());
        
        System.out.println("ðŸŽ® New game: " + sessionCode + " - " + playerName + " - Round 1 of " + totalRounds);
        
        return session;
    }
    
    public GameSession getSession(String sessionCode) {
        return activeSessions.get(sessionCode);
    }
    
    public Map<String, Object> processGuess(String sessionCode, String guess, int points) {
        GameSession session = activeSessions.get(sessionCode);
        if (session == null) return null;
        
        // Check if round is still active (not completed)
        // For now, we'll just process guesses - we'll track if round is completed separately
        boolean isCorrect = guess.equalsIgnoreCase(session.getCurrentSongName());
        int earnedPoints = 0;
        boolean roundCompleted = false;
        
        if (isCorrect) {
            earnedPoints = points;
            session.setTotalScore(session.getTotalScore() + earnedPoints);
            session.setCorrectGuesses(session.getCorrectGuesses() + 1);
            roundCompleted = true; // Mark round as completed when correct
        }
        
        Map<String, Object> result = new HashMap<>();
        result.put("correct", isCorrect);
        result.put("pointsEarned", earnedPoints);
        result.put("correctSongName", session.getCurrentSongName());
        result.put("roundCompleted", roundCompleted);
        result.put("currentRound", session.getCurrentRound());
        result.put("totalRounds", session.getTotalRounds());
        
        return result;
    }
    
    public GameSession nextRound(String sessionCode) {
        GameSession session = activeSessions.get(sessionCode);
        if (session == null) return null;
        
        // Check if game is complete
        if (session.getCurrentRound() >= session.getTotalRounds()) {
            session.setIsActive(false);
            gameSessionRepository.save(session);
            return session;
        }
        
        // Get a new song that hasn't been used in this session
        Song nextSong = getNewSongForSession(sessionCode);
        if (nextSong == null) return null;
        
        songRepository.save(nextSong);
        
        // Track used song
        usedSongsPerSession.get(sessionCode).add(nextSong.getSpotifyId());
        
        session.setCurrentRound(session.getCurrentRound() + 1);
        session.setCurrentSongId(nextSong.getSpotifyId());
        session.setCurrentSongName(nextSong.getName());
        session.setArtistName(nextSong.getArtistName());
        session.setPreviewUrl(nextSong.getPreviewUrl());
        
        gameSessionRepository.save(session);
        
        System.out.println("ðŸŽ® Starting Round " + session.getCurrentRound() + " of " + session.getTotalRounds());
        
        return session;
    }
    
    // Get a new song that hasn't been used in this session yet
    private Song getNewSongForSession(String sessionCode) {
        java.util.Set<String> usedSongs = usedSongsPerSession.get(sessionCode);
        int maxAttempts = 20;
        
        for (int attempt = 0; attempt < maxAttempts; attempt++) {
            Song song = deezerService.getRandomSong();
            if (song != null && !usedSongs.contains(song.getSpotifyId())) {
                return song;
            }
        }
        
        // If all attempts fail, just return any song
        return deezerService.getRandomSong();
    }
    
    private String generateSessionCode() {
        return UUID.randomUUID().toString().substring(0, 6).toUpperCase();
    }
}