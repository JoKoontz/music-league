package com.musicleague.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;


/**
 * Aldreyous Smith
 * Spring 2026
 * Software Engineering II
 * Mode 2 Backend - Guess The Song
 *  
 *   */

@Entity
@Table(name = "game_sessions")
public class GameSession {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true)
    private String sessionCode;
    
    private String playerName;
    private Integer totalRounds;
    private Integer currentRound;
    private Integer totalScore;
    private Integer correctGuesses;
    private Boolean isActive;
    private LocalDateTime startTime;
    
    private String currentSongId;
    private String currentSongName;
    private String artistName;
    
    @Column(length = 1000)  // âœ… Increased from default 255
    private String previewUrl;
    
    // Constructors
    public GameSession() {}
    
    // Getters
    public Long getId() { return id; }
    public String getSessionCode() { return sessionCode; }
    public String getPlayerName() { return playerName; }
    public Integer getTotalRounds() { return totalRounds; }
    public Integer getCurrentRound() { return currentRound; }
    public Integer getTotalScore() { return totalScore; }
    public Integer getCorrectGuesses() { return correctGuesses; }
    public Boolean getIsActive() { return isActive; }
    public LocalDateTime getStartTime() { return startTime; }
    public String getCurrentSongId() { return currentSongId; }
    public String getCurrentSongName() { return currentSongName; }
    public String getArtistName() { return artistName; }
    public String getPreviewUrl() { return previewUrl; }
    
    // Setters
    public void setId(Long id) { this.id = id; }
    public void setSessionCode(String sessionCode) { this.sessionCode = sessionCode; }
    public void setPlayerName(String playerName) { this.playerName = playerName; }
    public void setTotalRounds(Integer totalRounds) { this.totalRounds = totalRounds; }
    public void setCurrentRound(Integer currentRound) { this.currentRound = currentRound; }
    public void setTotalScore(Integer totalScore) { this.totalScore = totalScore; }
    public void setCorrectGuesses(Integer correctGuesses) { this.correctGuesses = correctGuesses; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }
    public void setStartTime(LocalDateTime startTime) { this.startTime = startTime; }
    public void setCurrentSongId(String currentSongId) { this.currentSongId = currentSongId; }
    public void setCurrentSongName(String currentSongName) { this.currentSongName = currentSongName; }
    public void setArtistName(String artistName) { this.artistName = artistName; }
    public void setPreviewUrl(String previewUrl) { this.previewUrl = previewUrl; }
}