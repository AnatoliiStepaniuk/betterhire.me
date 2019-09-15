package com.sdehunt.commons.model;

import lombok.Data;

@Data
public class BestSolution {
    private String userId;
    private String taskId;
    private String solutionId;
    private int rank; // TODO change to enum
    private long score;
    private boolean test;
}
