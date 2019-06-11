package com.sdehunt.repository.impl;

import com.sdehunt.commons.model.SolutionStatus;
import com.sdehunt.repository.SolutionQuery;
import lombok.Getter;

import java.util.Optional;

public class SolutionQueryImpl implements SolutionQuery {

    private String user;

    private String task;

    private SolutionStatus status;

    @Getter
    private boolean test;

    public SolutionQueryImpl user(String user) { // TODO setters lombok?
        this.user = user;
        return this;
    }

    public SolutionQueryImpl task(String task) {
        this.task = task;
        return this;
    }

    public SolutionQueryImpl status(SolutionStatus status) {
        this.status = status;
        return this;
    }

    public SolutionQueryImpl test(boolean test) {
        this.test = test;
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
