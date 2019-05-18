package com.sdehunt.commons.model;

import com.sdehunt.commons.TaskID;

import java.time.Instant;

public interface Solution {

    /**
     * Solution identifier
     */
    String getId();

    /**
     * Id of the task this solutions corresponds to
     */
    TaskID getTaskId();

    /**
     * Id of user that submitted this solution
     */
    String getUserId();

    /**
     * Identifier of user repository on github (username/reponame)
     */
    String getRepo();

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


    /*
        Setters
     */

    Solution setId(String id);

    Solution setTaskId(TaskID taskId);

    Solution setUserId(String userId);

    Solution setRepo(String repo);

    Solution setCommit(String commit);

    Solution setScore(long score);

    Solution setCreated(Instant created);
}
