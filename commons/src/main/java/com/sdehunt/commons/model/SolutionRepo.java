package com.sdehunt.commons.model;

import lombok.Data;

import java.time.Instant;

@Data
public class SolutionRepo {

    private long id;
    private String taskId;
    private String userId;
    private String repo;
    private String webhookSecret;
    private Instant created;

}
