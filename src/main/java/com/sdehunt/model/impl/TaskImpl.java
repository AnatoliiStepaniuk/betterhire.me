package com.sdehunt.model.impl;

import com.sdehunt.model.Task;

public class TaskImpl implements Task{

    private String id;
    private String description;

    public TaskImpl() {
    }

    public TaskImpl(String id, String description) {
        this.id = id;
        this.description = description;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getDescription() {
        return description;
    }
}
