package com.sdehunt.commons.model;

import com.sdehunt.commons.TaskID;
import lombok.Data;

import java.time.Instant;
import java.util.Set;

@Data
public class ShortTask {

    private TaskID id;
    private String name;
    private String imageUrl;
    private String shortDescription;
    private Integer participants;
    private Integer offers;
    private Integer bestOffer;
    private Set<Tag> tags;
    private Instant created;
    private Instant updated;
    private boolean enabled;
    private boolean submittable;
    private boolean test;
}
