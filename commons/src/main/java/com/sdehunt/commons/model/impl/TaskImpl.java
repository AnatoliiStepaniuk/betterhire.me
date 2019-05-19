package com.sdehunt.commons.model.impl;

import com.sdehunt.commons.TaskID;
import com.sdehunt.commons.model.Task;
import lombok.Data;

import java.time.Instant;

@Data
public class TaskImpl implements Task{

    private TaskID id;
    private String description;
    private Instant created;
    private Instant updated;

    public TaskImpl() {
    }

    public TaskImpl(TaskID id, String description, Instant created, Instant updated) {
        this.id = id;
        this.description = description;
        this.created = created;
        this.updated = updated;
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

    @Override
    public Instant getUpdated() {
        return updated;
    }
}
