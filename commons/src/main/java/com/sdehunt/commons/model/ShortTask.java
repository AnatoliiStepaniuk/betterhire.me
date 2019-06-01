package com.sdehunt.commons.model;

import com.sdehunt.commons.TaskID;
import lombok.Data;

import java.time.Instant;

@Data
public class ShortTask {

    private TaskID id;
    private String name;
    private String imageUrl; // TODO read from DB
    private String shortDescription;
    private Instant created;
    private Instant updated;
    private boolean enabled; // TODO read DB
    private boolean submittable; // TODO read DB
}
