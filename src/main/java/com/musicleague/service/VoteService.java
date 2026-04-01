package com.musicleague.service;

import java.util.List;
import org.springframework.stereotype.Service;
import com.musicleague.dto.VoteDto;
import com.musicleague.model.Round;
import com.musicleague.model.SongSubmission;
import com.musicleague.model.User;
import com.musicleague.model.Vote;
import com.musicleague.repository.VoteRepository;

@Service
public class VoteService {

    private static final int MAX_TOTAL_POINTS = 10;

    private final VoteRepository voteRepository;
    private final SubmissionService submissionService;

    public VoteService(VoteRepository voteRepository, SubmissionService submissionService) {
        this.voteRepository = voteRepository;
        this.submissionService = submissionService;
    }

    public Vote castVote(Round round, User voter, VoteDto dto) {
        if (!"VOTING_OPEN".equals(round.getStatus())) {
            throw new RuntimeException("Voting is not open.");
        }

        SongSubmission submission = submissionService.getSubmissionById(dto.getSubmissionId());

        if (submission.getUser().getId().equals(voter.getId())) {
            throw new RuntimeException("You cannot vote for your own song.");
        }

        int currentPoints = voteRepository.findByRoundAndVoter(round, voter)
                .stream()
                .mapToInt(Vote::getPointsGiven)
                .sum();

        if (currentPoints + dto.getPointsGiven() > MAX_TOTAL_POINTS) {
            throw new RuntimeException("Vote limit exceeded.");
        }

        Vote vote = new Vote(round, voter, submission, dto.getPointsGiven());
        return voteRepository.save(vote);
    }

    public List<Vote> getVotesForRound(Round round) {
        return voteRepository.findByRound(round);
    }
}