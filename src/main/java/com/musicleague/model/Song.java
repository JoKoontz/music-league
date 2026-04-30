package com.musicleague.model;

import jakarta.persistence.*;


/**
 * Aldreyous Smith
 * Spring 2026
 * Software Engineering II
 * Mode 2 Backend - Guess The Song
 *  
 *   */

@Entity
@Table(name = "songs")
public class Song {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "spotify_id")
    private String spotifyId;
    
    @Column(name = "name")
    private String name;
    
    @Column(name = "artist_name")
    private String artistName;
    
    @Column(name = "preview_url", length = 1000)  // âœ… Increased from default 255
    private String previewUrl;
    
    @Column(name = "album_art_url", length = 1000)  // âœ… Increased for album art URLs too
    private String albumArtUrl;
    
    @Column(name = "duration_ms")
    private Integer durationMs;
    
    public Song() {}
    
    public Song(String spotifyId, String name, String artistName, String previewUrl) {
        this.spotifyId = spotifyId;
        this.name = name;
        this.artistName = artistName;
        this.previewUrl = previewUrl;
    }
    
    public Song(String spotifyId, String name, String artistName, String previewUrl, String albumArtUrl, Integer durationMs) {
        this.spotifyId = spotifyId;
        this.name = name;
        this.artistName = artistName;
        this.previewUrl = previewUrl;
        this.albumArtUrl = albumArtUrl;
        this.durationMs = durationMs;
    }
    
    // Getters
    public Long getId() { return id; }
    public String getSpotifyId() { return spotifyId; }
    public String getName() { return name; }
    public String getSongName() { return name; }
    public String getArtistName() { return artistName; }
    public String getPreviewUrl() { return previewUrl; }
    public String getAlbumArtUrl() { return albumArtUrl; }
    public Integer getDurationMs() { return durationMs; }
    
    // Setters
    public void setId(Long id) { this.id = id; }
    public void setSpotifyId(String spotifyId) { this.spotifyId = spotifyId; }
    public void setName(String name) { this.name = name; }
    public void setArtistName(String artistName) { this.artistName = artistName; }
    public void setPreviewUrl(String previewUrl) { this.previewUrl = previewUrl; }
    public void setAlbumArtUrl(String albumArtUrl) { this.albumArtUrl = albumArtUrl; }
    public void setDurationMs(Integer durationMs) { this.durationMs = durationMs; }
}