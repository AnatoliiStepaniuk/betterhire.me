package com.sdehunt.model;

import java.util.List;

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
     * List of uri of uploaded files
     */
    List<String> getFiles();

    /**
     * Score amount this solution received
     */
    long getScore();

    // TODO add creation time
}
