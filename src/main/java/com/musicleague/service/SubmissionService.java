package com.musicleague.service;

import java.util.List;
import org.springframework.stereotype.Service;
import com.musicleague.dto.SubmissionDto;
import com.musicleague.model.Round;
import com.musicleague.model.SongSubmission;
import com.musicleague.model.User;
import com.musicleague.repository.SongSubmissionRepository;

@Service
public class SubmissionService {

    private final SongSubmissionRepository submissionRepository;

    public SubmissionService(SongSubmissionRepository submissionRepository) {
        this.submissionRepository = submissionRepository;
    }

    public SongSubmission submitSong(Round round, User user, SubmissionDto dto) {
        if (!"SUBMISSION_OPEN".equals(round.getStatus())) {
            throw new RuntimeException("Submissions are closed.");
        }

        if (submissionRepository.findByRoundAndUser(round, user).isPresent()) {
            throw new RuntimeException("You already submitted a song for this round.");
        }

        SongSubmission submission = new SongSubmission(
                round,
                user,
                dto.getSongTitle(),
                dto.getArtistName()
        );

        return submissionRepository.save(submission);
    }

    public List<SongSubmission> getSubmissionsForRound(Round round) {
        return submissionRepository.findByRound(round);
    }

    public SongSubmission getSubmissionById(Long id) {
        return submissionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Submission not found."));
    }
}