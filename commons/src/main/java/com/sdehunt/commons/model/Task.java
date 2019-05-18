package com.sdehunt.commons.model;

import com.sdehunt.commons.TaskID;

import java.time.Instant;

public interface Task {

    /**
     * Task identifier
     */
    TaskID getId();
    // TODO add name
    /**
     * Task description in Markdown format
     */
    String getDescription();

    /**
     * Instant when the task was created
     */
    Instant getCreated(); // TODO do something with json instant mapping and add it to tests

    /**
     * Instant when the task was updated
     */
    Instant getUpdated();

}
