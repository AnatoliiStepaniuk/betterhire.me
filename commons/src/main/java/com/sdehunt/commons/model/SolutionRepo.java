package com.sdehunt.commons.model;

import com.sdehunt.commons.TaskID;
import lombok.Data;

import java.time.Instant;

@Data
public class SolutionRepo {

    private long id;
    private TaskID taskID;
    private String userId;
    private String repo;
    private String webhookSecret;
    private Instant created;

}
