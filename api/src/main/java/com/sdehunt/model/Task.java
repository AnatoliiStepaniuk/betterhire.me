package com.sdehunt.model;

import com.sdehunt.commons.TaskID;

import java.time.Instant;

public interface Task {

    /**
     * Task identifier
     */
    TaskID getId();

    /**
     * Task description in Markdown format
     */
    String getDescription();

    /**
     * Instant when the task was created
     */
    Instant getCreated();
}
