package com.sdehunt.commons.model;

import com.sdehunt.commons.TaskID;
import lombok.Data;

@Data
public class SolutionRepo {

    private TaskID taskID;
    private String userId;
    private String repo;
    private String webhookSecret;

}
