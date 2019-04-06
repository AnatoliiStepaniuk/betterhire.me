package com.sdehunt.model.impl;

import com.sdehunt.model.Solution;

import java.time.Instant;

public class SolutionImpl implements Solution{

    private String id;
    private String taskId;
    private String userId;
    private String repo;
    private String commit;
    private long score;
    private Instant created;

    public SolutionImpl() {
    }

    public SolutionImpl(
            String id,
            String taskId,
            String userId,
            String repo,
            String commit,
            long score,
            Instant created
    ) {
        this.id = id;
        this.taskId = taskId;
        this.userId = userId;
        this.repo = repo;
        this.commit = commit;
        this.score = score;
        this.created = created;
    }

    public SolutionImpl withTaskId(String taskId) {
        return new SolutionImpl(
                this.id,
                taskId,
                this.userId,
                this.repo,
                this.commit,
                this.score,
                this.created
        );
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getTaskId() {
        return taskId;
    }

    @Override
    public String getUserId() {
        return userId;
    }

    @Override
    public String getRepo() {
        return repo;
    }

    @Override
    public String getCommit() {
        return commit;
    }

    @Override
    public long getScore() {
        return score;
    }

    @Override
    public Instant getCreated() {
        return created;
    }
}
