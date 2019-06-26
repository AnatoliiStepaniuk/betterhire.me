package com.sdehunt.commons.model;

import com.sdehunt.commons.TaskID;
import lombok.Data;

@Data
public class BestSolution {
    private String userId;
    private TaskID taskID;
    private String solutionId;
    private int rank; // TODO change to enum
    private long score;
    private boolean test;
}
