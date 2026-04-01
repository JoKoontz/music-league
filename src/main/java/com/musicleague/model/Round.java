package com.musicleague.model;

import java.time.LocalDateTime;
import jakarta.persistence.*;

@Entity
@Table(name = "league_rounds")
public class Round {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "league_id", nullable = false)
    private League league;

    @Column(nullable = false)
    private String theme;

    @Column(nullable = false)
    private LocalDateTime submissionDeadline;

    @Column(nullable = false)
    private LocalDateTime votingDeadline;

    @Column(nullable = false)
    private String status; // SUBMISSION_OPEN, VOTING_OPEN, CLOSED

    public Round() {
    }

    public Round(League league, String theme, LocalDateTime submissionDeadline,
                 LocalDateTime votingDeadline, String status) {
        this.league = league;
        this.theme = theme;
        this.submissionDeadline = submissionDeadline;
        this.votingDeadline = votingDeadline;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public League getLeague() {
        return league;
    }

    public void setLeague(League league) {
        this.league = league;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public LocalDateTime getSubmissionDeadline() {
        return submissionDeadline;
    }

    public void setSubmissionDeadline(LocalDateTime submissionDeadline) {
        this.submissionDeadline = submissionDeadline;
    }

    public LocalDateTime getVotingDeadline() {
        return votingDeadline;
    }

    public void setVotingDeadline(LocalDateTime votingDeadline) {
        this.votingDeadline = votingDeadline;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}