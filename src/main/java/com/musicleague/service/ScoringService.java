package com.musicleague.service;

import java.util.*;
import org.springframework.stereotype.Service;
import com.musicleague.model.League;
import com.musicleague.model.LeagueMember;
import com.musicleague.model.Round;
import com.musicleague.model.SongSubmission;
import com.musicleague.model.Vote;
import com.musicleague.repository.LeagueMemberRepository;

@Service
public class ScoringService {

    private final SubmissionService submissionService;
    private final VoteService voteService;
    private final LeagueMemberRepository leagueMemberRepository;

    public ScoringService(SubmissionService submissionService,
                          VoteService voteService,
                          LeagueMemberRepository leagueMemberRepository) {
        this.submissionService = submissionService;
        this.voteService = voteService;
        this.leagueMemberRepository = leagueMemberRepository;
    }

    public Map<SongSubmission, Integer> getRoundScores(Round round) {
        List<SongSubmission> submissions = submissionService.getSubmissionsForRound(round);
        List<Vote> votes = voteService.getVotesForRound(round);

        Map<SongSubmission, Integer> scores = new HashMap<>();

        for (SongSubmission submission : submissions) {
            int total = 0;
            for (Vote vote : votes) {
                if (vote.getSubmission().getId().equals(submission.getId())) {
                    total += vote.getPointsGiven();
                }
            }
            scores.put(submission, total);
        }

        return scores;
    }

    public void applyRoundScoresToLeague(Round round) {
        Map<SongSubmission, Integer> scores = getRoundScores(round);
        League league = round.getLeague();

        for (Map.Entry<SongSubmission, Integer> entry : scores.entrySet()) {
            SongSubmission submission = entry.getKey();
            Integer score = entry.getValue();

            LeagueMember member = leagueMemberRepository
                    .findByLeagueAndUser(league, submission.getUser())
                    .orElseThrow(() -> new RuntimeException("League member not found."));

            member.setTotalPoints(member.getTotalPoints() + score);
            leagueMemberRepository.save(member);
        }
    }

    public List<LeagueMember> getLeaderboard(League league) {
        List<LeagueMember> members = leagueMemberRepository.findByLeague(league);
        members.sort((a, b) -> Integer.compare(b.getTotalPoints(), a.getTotalPoints()));
        return members;
    }
}