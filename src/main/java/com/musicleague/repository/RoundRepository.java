package com.musicleague.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.musicleague.model.League;
import com.musicleague.model.Round;

public interface RoundRepository extends JpaRepository<Round, Long> {
    List<Round> findByLeague(League league);
}