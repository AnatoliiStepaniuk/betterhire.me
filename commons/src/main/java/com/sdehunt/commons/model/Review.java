package com.sdehunt.commons.model;

import com.sdehunt.commons.TaskID;
import lombok.Data;

import java.time.Instant;

@Data
public class Review {

    private String id;
    private String userId;
    private TaskID taskID;
    private String solutionId;
    private Long grade;
    private String comment;
    private String emoji;
    private String reviewer;
    private Instant created;

}
