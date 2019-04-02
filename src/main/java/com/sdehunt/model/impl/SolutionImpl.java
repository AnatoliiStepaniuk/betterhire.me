package com.sdehunt.model.impl;

import com.sdehunt.model.Solution;

import java.util.List;

public class SolutionImpl implements Solution{

    private String id;
    private String taskId;
    private String userId;
    private List<String> files;
    private long score;

    public SolutionImpl() {
    }

    public SolutionImpl(String id, String taskId, String userId, List<String> files, long score) {
        this.id = id;
        this.taskId = taskId;
        this.userId = userId;
        this.files = files;
        this.score = score;
    }

    public SolutionImpl withTaskId(String taskId) {
        return new SolutionImpl(
                this.id,
                taskId,
                this.userId,
                this.files,
                this.score
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
    public List<String> getFiles() {
        return files;
    }

    @Override
    public long getScore() {
        return score;
    }
}
