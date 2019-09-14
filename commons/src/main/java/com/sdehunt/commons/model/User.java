package com.sdehunt.commons.model;

import lombok.Data;

import java.time.Instant;
import java.util.Set;

@Data
public class User {

    private String id;
    private String name;
    private String nickname;
    private String imageUrl;
    private String email;
    private String cv;
    private String phone;
    private String githubLogin;
    private String linkedinId;
    private String company;
    private String userName; // This field is not saved in DB actually
    private String city;
    private Set<Language> languages;
    private Instant lastSubmit;
    private Integer solved;
    private Integer avgRank;
    private Instant created;
    private Instant updated;
    private boolean test;
    private Boolean manager;
    private Boolean available;

    public User() {
    }

    public User(User u) {
        this.id = u.getId();
        this.name = u.getName();
        this.nickname = u.getNickname();
        this.imageUrl = u.getImageUrl();
        this.email = u.getEmail();
        this.cv = u.getCv();
        this.phone = u.getPhone();
        this.githubLogin = u.getGithubLogin();
        this.linkedinId = u.getLinkedinId();
        this.userName = u.getUserName();
        this.city = u.getCity();
        this.languages = u.getLanguages();
        this.lastSubmit = u.getLastSubmit();
        this.solved = u.getSolved();
        this.avgRank = u.getAvgRank();
        this.created = u.getCreated();
        this.updated = u.getUpdated();
        this.test = u.isTest();
        this.available = u.getAvailable();
        this.manager = u.getManager();
    }
}
