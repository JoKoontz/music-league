package com.musicleague.service;

import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;
import com.musicleague.dto.LeagueDto;
import com.musicleague.model.League;
import com.musicleague.model.LeagueMember;
import com.musicleague.model.User;
import com.musicleague.repository.LeagueMemberRepository;
import com.musicleague.repository.LeagueRepository;

@Service
public class LeagueService {

    private final LeagueRepository leagueRepository;
    private final LeagueMemberRepository leagueMemberRepository;

    public LeagueService(LeagueRepository leagueRepository,
                         LeagueMemberRepository leagueMemberRepository) {
        this.leagueRepository = leagueRepository;
        this.leagueMemberRepository = leagueMemberRepository;
    }

    public League createLeague(LeagueDto dto, User owner) {
        String inviteCode = UUID.randomUUID().toString().substring(0, 8);

        League league = new League(dto.getName(), inviteCode, owner);
        League savedLeague = leagueRepository.save(league);

        LeagueMember ownerMembership = new LeagueMember(savedLeague, owner);
        leagueMemberRepository.save(ownerMembership);

        return savedLeague;
    }

    public void joinLeague(String inviteCode, User user) {
        League league = leagueRepository.findByInviteCode(inviteCode)
                .orElseThrow(() -> new RuntimeException("League not found."));

        if (leagueMemberRepository.findByLeagueAndUser(league, user).isPresent()) {
            throw new RuntimeException("User is already in this league.");
        }

        leagueMemberRepository.save(new LeagueMember(league, user));
    }

    public League getLeagueById(Long id) {
        return leagueRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("League not found."));
    }

    public List<LeagueMember> getLeagueMembers(League league) {
        return leagueMemberRepository.findByLeague(league);
    }
}