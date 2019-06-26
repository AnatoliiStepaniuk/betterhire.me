package com.sdehunt.commons.model;

import lombok.Data;

import java.time.Instant;

@Data
public class User {

    private String id;
    private String name;
    private String nickname;
    private String imageUrl;
    private String email;
    private String githubLogin;
    private String linkedinId;
    private String userName;
    private Instant lastSubmit;
    private int solved;
    private int avgRank;
    private Instant created;
    private Instant updated;
    private boolean test;

}
