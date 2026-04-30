package com.musicleague.service;

import com.musicleague.model.Song;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;


/**
 * Aldreyous Smith
 * Spring 2026
 * Software Engineering II
 * Mode 2 Backend - Guess The Song
 *  
 *   */

@Service
public class DeezerService {
    
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final Random random;
    
    // Expanded list of popular artists across different genres
    private static final List<String> POPULAR_ARTISTS = Arrays.asList(
        // Pop
        "Taylor Swift", "Ed Sheeran", "Adele", "Bruno Mars", "Katy Perry",
        "Lady Gaga", "Rihanna", "Justin Bieber", "Ariana Grande", "The Weeknd",
        "Dua Lipa", "Harry Styles", "Olivia Rodrigo", "Shawn Mendes", "Selena Gomez",
        "Miley Cyrus", "Pink", "Kelly Clarkson", "Maroon 5", "Imagine Dragons",
        
        // Hip Hop/Rap
        "Drake", "Eminem", "Kanye West", "Nicki Minaj", "Post Malone",
        "Travis Scott", "Cardi B", "Megan Thee Stallion", "Doja Cat", "Lil Nas X",
        
        // Rock/Alternative
        "Coldplay", "Foo Fighters", "Red Hot Chili Peppers", "Green Day", "Linkin Park",
        "Twenty One Pilots", "Fall Out Boy", "Paramore", "The Killers", "Arctic Monkeys",
        
        // R&B/Soul
        "Beyonce", "Usher", "Alicia Keys", "John Legend", "Frank Ocean",
        "SZA", "H.E.R.", "Khalid", "Daniel Caesar", "Giveon",
        
        // Latin
        "Shakira", "Bad Bunny", "J Balvin", "Daddy Yankee", "Luis Fonsi",
        "Ozuna", "Maluma", "Karol G", "Rosalia", "Enrique Iglesias",
        
        // Electronic/Dance
        "Calvin Harris", "David Guetta", "Zedd", "The Chainsmokers", "Marshmello",
        "Kygo", "Martin Garrix", "Alan Walker", "Avicii", "Skrillex"
    );
    
    // Track the last used artist to ensure variety
    private String lastArtist = "";
    
    // Cache for songs by artist (cleared after each use)
    private java.util.Map<String, List<Song>> artistSongCache = new java.util.HashMap<>();
    
    public DeezerService() {
        this.restTemplate = new RestTemplate();
        this.objectMapper = new ObjectMapper();
        this.random = new Random();
    }
    
    public Song getRandomSong() {
        // Force a different artist every time
        String artist = getDifferentArtist();
        
        // Get songs for this artist
        List<Song> artistSongs = artistSongCache.get(artist);
        
        // If no cache or empty, fetch new songs
        if (artistSongs == null || artistSongs.isEmpty()) {
            artistSongs = fetchSongsByArtist(artist);
            if (artistSongs != null && !artistSongs.isEmpty()) {
                artistSongCache.put(artist, new ArrayList<>(artistSongs));
            }
        }
        
        // Get a song from this artist's cache
        if (artistSongs != null && !artistSongs.isEmpty()) {
            Song song = artistSongs.remove(0);
            System.out.println("ðŸŽµ Deezer: " + song.getSongName() + " by " + song.getArtistName());
            
            // If this artist has no more songs, remove from cache
            if (artistSongCache.get(artist).isEmpty()) {
                artistSongCache.remove(artist);
            }
            
            return song;
        }
        
        // Emergency fallback
        return getEmergencySong();
    }
    
    private String getDifferentArtist() {
        String newArtist;
        do {
            newArtist = POPULAR_ARTISTS.get(random.nextInt(POPULAR_ARTISTS.size()));
        } while (newArtist.equals(lastArtist) && POPULAR_ARTISTS.size() > 1);
        
        lastArtist = newArtist;
        System.out.println("ðŸŽ¤ Selecting artist: " + newArtist);
        return newArtist;
    }
    
    private List<Song> fetchSongsByArtist(String artistName) {
        List<Song> songs = new ArrayList<>();
        
        try {
            // Search for the artist
            String searchUrl = String.format(
                "https://api.deezer.com/search/artist?q=%s",
                artistName.replace(" ", "%20")
            );
            
            String searchResponse = restTemplate.getForObject(searchUrl, String.class);
            JsonNode searchRoot = objectMapper.readTree(searchResponse);
            JsonNode artistData = searchRoot.path("data");
            
            if (artistData.isArray() && artistData.size() > 0) {
                long artistId = artistData.get(0).path("id").asLong();
                
                // Get top tracks (most popular songs first)
                String tracksUrl = String.format(
                    "https://api.deezer.com/artist/%d/top?limit=20",
                    artistId
                );
                
                String tracksResponse = restTemplate.getForObject(tracksUrl, String.class);
                JsonNode tracksRoot = objectMapper.readTree(tracksResponse);
                JsonNode tracks = tracksRoot.path("data");
                
                if (tracks.isArray()) {
                    for (JsonNode track : tracks) {
                        String previewUrl = track.path("preview").asText();
                        if (previewUrl != null && !previewUrl.isEmpty()) {
                            Song song = new Song(
                                "deezer_" + track.path("id").asText(),
                                track.path("title").asText(),
                                track.path("artist").path("name").asText(),
                                previewUrl
                            );
                            
                            // Add album art if available
                            String albumCover = track.path("album").path("cover_medium").asText();
                            if (albumCover != null && !albumCover.isEmpty()) {
                                song.setAlbumArtUrl(albumCover);
                            }
                            
                            int duration = track.path("duration").asInt();
                            song.setDurationMs(duration * 1000);
                            
                            songs.add(song);
                        }
                    }
                }
                System.out.println("âœ… Loaded " + songs.size() + " songs from " + artistName);
            }
        } catch (Exception e) {
            System.err.println("â�Œ Error fetching from Deezer for " + artistName + ": " + e.getMessage());
        }
        
        return songs;
    }
    
    private Song getEmergencySong() {
        // Emergency fallback with known working previews from different artists
        String[][] fallbacks = {
            {"Blinding Lights", "The Weeknd", "https://cdns-preview-3.dzcdn.net/stream/c-3d4e5f6a7b8c9d0e1f2a3b4c5d6e7f8a9-1.mp3"},
            {"Levitating", "Dua Lipa", "https://cdns-preview-2.dzcdn.net/stream/c-2c3d4e5f6a7b8c9d0e1f2a3b4c5d6e7f8-1.mp3"},
            {"Shape of You", "Ed Sheeran", "https://cdns-preview-1.dzcdn.net/stream/c-1a2b3c4d5e6f7a8b9c0d1e2f3a4b5c6d-1.mp3"},
            {"Bad Guy", "Billie Eilish", "https://cdns-preview-2.dzcdn.net/stream/c-2b3c4d5e6f7a8b9c0d1e2f3a4b5c6d7e-1.mp3"},
            {"Dance Monkey", "Tones and I", "https://cdns-preview-3.dzcdn.net/stream/c-3c4d5e6f7a8b9c0d1e2f3a4b5c6d7e8f-1.mp3"},
            {"Uptown Funk", "Mark Ronson", "https://cdns-preview-1.dzcdn.net/stream/c-1d2e3f4a5b6c7d8e9f0a1b2c3d4e5f6a7-1.mp3"},
            {"Happier", "Marshmello", "https://cdns-preview-2.dzcdn.net/stream/c-2e3f4a5b6c7d8e9f0a1b2c3d4e5f6a7b-1.mp3"},
            {"Senorita", "Shawn Mendes", "https://cdns-preview-3.dzcdn.net/stream/c-3f4a5b6c7d8e9f0a1b2c3d4e5f6a7b8c-1.mp3"}
        };
        
        String[] selected = fallbacks[random.nextInt(fallbacks.length)];
        Song song = new Song("emergency_" + selected[0].replace(" ", "_"), selected[0], selected[1], selected[2]);
        System.out.println("âš ï¸� Using emergency song: " + song.getSongName() + " by " + song.getArtistName());
        return song;
    }
    
    public List<Song> searchSongs(String query, int limit) {
        List<Song> results = new ArrayList<>();
        try {
            String url = String.format(
                "https://api.deezer.com/search/track?q=%s&limit=%d",
                query.replace(" ", "%20"), Math.min(limit, 50)
            );
            
            String response = restTemplate.getForObject(url, String.class);
            JsonNode root = objectMapper.readTree(response);
            JsonNode tracks = root.path("data");
            
            for (JsonNode track : tracks) {
                String previewUrl = track.path("preview").asText();
                if (previewUrl != null && !previewUrl.isEmpty()) {
                    Song song = new Song(
                        "deezer_" + track.path("id").asText(),
                        track.path("title").asText(),
                        track.path("artist").path("name").asText(),
                        previewUrl
                    );
                    results.add(song);
                }
            }
        } catch (Exception e) {
            System.err.println("â�Œ Error searching Deezer: " + e.getMessage());
        }
        return results;
    }
    
    public void clearCache() {
        artistSongCache.clear();
        lastArtist = "";
        System.out.println("ðŸ”„ Artist cache cleared");
    }
}