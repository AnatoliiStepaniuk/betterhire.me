package com.sdehunt.model;

public interface Task {
    /**
     * Task identifier
     */
    String getId(); //TODO int of UUID? maybe UUID in binary form?

    /**
     * Task description in Markdown format
     */
    String getDescription();
}
