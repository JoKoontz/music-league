package com.musicleague.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.musicleague.model.Round;
import com.musicleague.model.SongSubmission;
import com.musicleague.model.User;

public interface SongSubmissionRepository extends JpaRepository<SongSubmission, Long> {
    List<SongSubmission> findByRound(Round round);
    Optional<SongSubmission> findByRoundAndUser(Round round, User user);
}