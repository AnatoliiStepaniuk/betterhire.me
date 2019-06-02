package com.sdehunt.commons.exception;

import com.sdehunt.commons.TaskID;

public class InvalidSolutionException extends RuntimeException {
    public InvalidSolutionException() {
    }

    public InvalidSolutionException(String message) {
        super(message);
    }

    public InvalidSolutionException(TaskID taskID, String solutionId) {
        super("Invalid solution (" + solutionId + ") for task " + taskID);
    }
}
