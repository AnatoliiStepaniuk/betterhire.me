package com.sdehunt.commons.model;

import lombok.Data;

import java.time.Instant;

@Data
public class User {

    private String id;
    private String nickname; // TODO add to tests
    private String email;
    private String github;
    private String linkedIn;
    private Instant created;
    private Instant updated;

}
