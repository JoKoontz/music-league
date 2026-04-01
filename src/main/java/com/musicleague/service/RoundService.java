package com.musicleague.service;

import java.util.List;
import org.springframework.stereotype.Service;
import com.musicleague.dto.RoundDto;
import com.musicleague.model.League;
import com.musicleague.model.Round;
import com.musicleague.repository.RoundRepository;

@Service
public class RoundService {

    private final RoundRepository roundRepository;

    public RoundService(RoundRepository roundRepository) {
        this.roundRepository = roundRepository;
    }

    public Round createRound(League league, RoundDto dto) {
        Round round = new Round(
                league,
                dto.getTheme(),
                dto.getSubmissionDeadline(),
                dto.getVotingDeadline(),
                "SUBMISSION_OPEN"
        );

        return roundRepository.save(round);
    }

    public Round getRoundById(Long id) {
        return roundRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Round not found."));
    }

    public List<Round> getRoundsForLeague(League league) {
        return roundRepository.findByLeague(league);
    }

    public void updateRoundStatus(Round round) {
        if (round.getVotingDeadline().isBefore(java.time.LocalDateTime.now())) {
            round.setStatus("CLOSED");
        } else if (round.getSubmissionDeadline().isBefore(java.time.LocalDateTime.now())) {
            round.setStatus("VOTING_OPEN");
        } else {
            round.setStatus("SUBMISSION_OPEN");
        }

        roundRepository.save(round);
    }
}