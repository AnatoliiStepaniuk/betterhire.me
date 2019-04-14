package com.sdehunt.commons.model.impl;

import com.sdehunt.commons.TaskID;
import com.sdehunt.commons.model.Task;

import java.time.Instant;

public class TaskImpl implements Task{

    private TaskID id;
    private String description;
    private Instant created;

    public TaskImpl() {
    }

    public TaskImpl(TaskID id, String description, Instant created) {
        this.id = id;
        this.description = description;
        this.created = created;
    }

    public TaskImpl(TaskID id, String description) {
        this.id = id;
        this.description = description;
    }

    @Override
    public TaskID getId() {
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
