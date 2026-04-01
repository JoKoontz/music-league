package com.musicleague.model;

import jakarta.persistence.*;

@Entity
public class Vote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "round_id", nullable = false)
    private Round round;

    @ManyToOne
    @JoinColumn(name = "voter_id", nullable = false)
    private User voter;

    @ManyToOne
    @JoinColumn(name = "submission_id", nullable = false)
    private SongSubmission submission;

    @Column(nullable = false)
    private int pointsGiven;

    public Vote() {
    }

    public Vote(Round round, User voter, SongSubmission submission, int pointsGiven) {
        this.round = round;
        this.voter = voter;
        this.submission = submission;
        this.pointsGiven = pointsGiven;
    }

    public Long getId() {
        return id;
    }

    public Round getRound() {
        return round;
    }

    public void setRound(Round round) {
        this.round = round;
    }

    public User getVoter() {
        return voter;
    }

    public void setVoter(User voter) {
        this.voter = voter;
    }

    public SongSubmission getSubmission() {
        return submission;
    }

    public void setSubmission(SongSubmission submission) {
        this.submission = submission;
    }

    public int getPointsGiven() {
        return pointsGiven;
    }

    public void setPointsGiven(int pointsGiven) {
        this.pointsGiven = pointsGiven;
    }
}