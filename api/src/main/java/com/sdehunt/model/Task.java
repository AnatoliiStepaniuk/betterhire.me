package com.sdehunt.model;

import java.time.Instant;

public interface Task {

    /**
     * Task identifier
     */
    String getId(); //TODO int of UUID? maybe UUID in binary form?

    /**
     * Task description in Markdown format
     */
    String getDescription();

    /**
     * Instant when the task was created
     */
    Instant getCreated();
}
