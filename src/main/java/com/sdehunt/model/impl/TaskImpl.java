package com.sdehunt.model.impl;

import com.sdehunt.model.Task;

import java.time.Instant;

public class TaskImpl implements Task{

    private String id;
    private String description;
    private Instant created;

    public TaskImpl() {
    }

    public TaskImpl(String id, String description, Instant created) {
        this.id = id;
        this.description = description;
        this.created = created;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public Instant getCreated() {
        return created;
    }
}
