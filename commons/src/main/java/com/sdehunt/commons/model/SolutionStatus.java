package com.sdehunt.commons.model;

import java.util.Arrays;
import java.util.Collection;

public enum SolutionStatus {
    ACCEPTED,           // Solution was correct and was processed successfully
    IN_PROGRESS,        // Solution is not processed yet
    INVALID_FILES,      // Required solution files were not found in specified location
    INVALID_SOLUTION,   // Solution does not satisfy task requirements
    TIMEOUT,            // Solution processing took too long
    ERROR,              // General error, not covered by other statuses

    WAITING_FOR_REVIEW, // Waiting for company representative to review the task
    REVIEWED;            // The task was reviewed by company representative

    public static Collection<SolutionStatus> successful() {
        return Arrays.asList(ACCEPTED, WAITING_FOR_REVIEW, REVIEWED);
    }

    public static Collection<SolutionStatus> failed() {
        return Arrays.asList(INVALID_FILES, INVALID_SOLUTION, TIMEOUT, ERROR);
    }

}
