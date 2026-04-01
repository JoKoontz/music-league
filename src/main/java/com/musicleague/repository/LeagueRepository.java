package com.musicleague.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.musicleague.model.League;

public interface LeagueRepository extends JpaRepository<League, Long> {
    Optional<League> findByInviteCode(String inviteCode);
}