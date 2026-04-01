package com.musicleague.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.musicleague.model.League;
import com.musicleague.model.LeagueMember;
import com.musicleague.model.User;

public interface LeagueMemberRepository extends JpaRepository<LeagueMember, Long> {
    List<LeagueMember> findByLeague(League league);
    Optional<LeagueMember> findByLeagueAndUser(League league, User user);
}