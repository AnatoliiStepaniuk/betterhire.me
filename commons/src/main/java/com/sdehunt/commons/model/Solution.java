package com.sdehunt.commons.model;

import com.sdehunt.commons.TaskID;
import lombok.Data;

import java.time.Instant;

@Data
public class Solution {

    private String id;
    private TaskID taskId;
    private String userId;
    private String repo;
    private String commit;
    private long score;
    private SolutionStatus status;
    private Instant created;
    private boolean test;
    private String cause;

}
