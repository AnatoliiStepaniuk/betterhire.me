package com.sdehunt.commons.exception;

import com.sdehunt.commons.TaskID;
import lombok.Getter;

public class InvalidSolutionException extends RuntimeException {

    @Getter
    private String description;

    public InvalidSolutionException(String description) {
        this.description = description;
    }

    public InvalidSolutionException(TaskID taskID, String solutionId, String description) {
        super("Invalid solution (" + solutionId + ") for task " + taskID);
        this.description = description;
    }
}
