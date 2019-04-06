package com.sdehunt.model;

import java.time.Instant;

public interface Solution {
    /**
     * Solution identifier
     */
    String getId();

    /**
     * Id of the task this solutions corresponds to
     */
    String getTaskId();

    /**
     * Id of user that submitted this solution
     */
    String getUserId();

    /**
     * Id user repository
     */
    String getRepoId();

    /**
     * Commit of commit corresponding to current solution
     */
    String getCommit();

    /**
     * Score amount this solution received
     */
    long getScore();

    /**
     * Instant when solution was created
     */
    Instant getCreated();

}
