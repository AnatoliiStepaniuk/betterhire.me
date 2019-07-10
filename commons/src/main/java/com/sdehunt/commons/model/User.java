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
    private String userName; // This field is not saved in DB actually
    private Instant lastSubmit;
    private Integer solved;
    private Integer avgRank;
    private Instant created;
    private Instant updated;
    private boolean test;

}
