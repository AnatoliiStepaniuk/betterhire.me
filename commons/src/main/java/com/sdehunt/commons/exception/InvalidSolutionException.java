package com.sdehunt.commons.exception;

import lombok.Getter;

public class InvalidSolutionException extends RuntimeException {

    @Getter
    private String description;

    public InvalidSolutionException(String description) {
        this.description = description;
    }

    public InvalidSolutionException(String taskId, String solutionId, String description) {
        super("Invalid solution (" + solutionId + ") for task " + taskId);
        this.description = description;
    }
}
