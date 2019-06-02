package com.sdehunt.commons.model;

public enum SolutionStatus {
    ACCEPTED,           // Solution was correct and was processed successfully
    IN_PROGRESS,        // Solution is not processed yet
    INVALID_FILES,      // Required solution files were not found in specified location
    INVALID_SOLUTION,   // Solution does not satisfy task requirements
    TIMEOUT,            // Solution processing took too long
    ERROR               // General error, not covered by other statuses
}
