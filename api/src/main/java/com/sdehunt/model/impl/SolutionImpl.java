package com.sdehunt.model.impl;

import com.sdehunt.commons.TaskID;
import com.sdehunt.model.Solution;
import com.sdehunt.model.Task;

import java.time.Instant;

public class SolutionImpl implements Solution {

    private String id;
    private TaskID taskId;
    private String userId;
    private String repo;
    private String commit;
    private long score;
    private Instant created;

    public SolutionImpl() {
    }

    public SolutionImpl(
            String id,
            TaskID taskId,
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

    @Override
    public String getId() {
        return id;
    }

    @Override
    public TaskID getTaskId() {
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

    @Override
    public Solution withScore(long score) {
        return new SolutionImpl(
                this.id,
                this.taskId,
                this.userId,
                this.repo,
                this.commit,
                score,
                this.created
        );
    }

    // TODO move upper?
    public SolutionImpl withTaskId(String taskId) {
        return this.withTaskId(TaskID.valueOf(taskId.toUpperCase()));
    }

    public SolutionImpl withTaskId(TaskID taskId) {
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

}
