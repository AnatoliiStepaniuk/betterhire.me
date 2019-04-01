package com.sdehunt.model.impl;

import com.sdehunt.model.Solution;

import java.util.List;

public class SolutionImpl implements Solution{

    private String id;
    private String taskId;
    private String userId;
    private List<String> files;

    public SolutionImpl() {
    }

    public SolutionImpl(String id, String taskId, String userId, List<String> files) {
        this.id = id;
        this.taskId = taskId;
        this.userId = userId;
        this.files = files;
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
}
