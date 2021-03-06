package com.sdehunt.commons.model;

import lombok.Data;

import java.time.Instant;
import java.util.Set;

@Data
public class ShortTask {

    private String id;
    private TaskType type;
    private String name;
    private String imageUrl;
    private String shortDescription;
    private String company;
    private String city;
    private Integer participants;// TODO remove it is called users now
    private Integer users;
    private Integer offers;
    private Integer bestOffer;// TODO remove, we are showing lastSubmit now
    private Instant lastSubmit;
    private Set<String> tags;
    private Set<Language> languages;
    private Instant created;
    private boolean enabled;
    private boolean submittable;
    private boolean test;
}
