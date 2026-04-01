package com.musicleague.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.musicleague.model.Round;
import com.musicleague.model.User;
import com.musicleague.model.Vote;

public interface VoteRepository extends JpaRepository<Vote, Long> {
    List<Vote> findByRound(Round round);
    List<Vote> findByRoundAndVoter(Round round, User voter);
}