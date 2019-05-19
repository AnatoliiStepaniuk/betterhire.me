package com.sdehunt.commons.model;

import com.sdehunt.commons.TaskID;
import lombok.Data;

import java.time.Instant;

@Data
public class Task {

    private TaskID id;
    private String description;
    private Instant created;
    private Instant updated;

}
