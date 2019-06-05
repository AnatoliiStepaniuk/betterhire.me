package com.sdehunt.commons.model;

import lombok.Data;

import java.time.Instant;

@Data
public class User {

    private String id;
    private String name; // TODO? add to db + tests
    private String nickname; // TODO? add to db + tests
    private String imageUrl; // TODO? add to db + tests
    private String email;
    private String github;
    private String linkedIn;
    private Instant created;
    private Instant updated;

}
