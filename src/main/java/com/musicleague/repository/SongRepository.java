package com.musicleague.repository;

import com.musicleague.model.Song;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;


/**
 * Aldreyous Smith
 * Spring 2026
 * Software Engineering II
 * Mode 2 Backend - Guess The Song
 *  
 *   */

@Repository
public interface SongRepository extends JpaRepository<Song, Long> {
    Optional<Song> findBySpotifyId(String spotifyId);
}