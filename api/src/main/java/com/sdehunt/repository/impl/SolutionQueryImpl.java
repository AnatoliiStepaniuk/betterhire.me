package com.sdehunt.repository.impl;

import com.sdehunt.commons.model.SolutionStatus;
import com.sdehunt.repository.SolutionQuery;

import java.util.Optional;

public class SolutionQueryImpl implements SolutionQuery {

    private String user;

    private String task;

    private SolutionStatus status;

    public SolutionQueryImpl withUser(String user) {
        this.user = user;
        return this;
    }

    public SolutionQueryImpl withTask(String task) {
        this.task = task;
        return this;
    }

    public SolutionQueryImpl withStatus(SolutionStatus status) {
        this.status = status;
        return this;
    }

    @Override
    public Optional<String> getUser() {
        return Optional.ofNullable(user);
    }

    @Override
    public Optional<String> getTask() {
        return Optional.ofNullable(task);
    }

    @Override
    public Optional<SolutionStatus> getStatus() {
        return Optional.ofNullable(status);
    }

}
