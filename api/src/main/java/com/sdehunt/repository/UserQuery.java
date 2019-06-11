package com.sdehunt.repository;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Optional;

@ToString
public class UserQuery {
    @Setter
    private String nickname;
    @Setter
    private String email;
    @Setter
    private String githubLogin;
    @Setter
    private String linkedinId;
    @Setter
    @Getter
    private boolean test;

    public Optional<String> getNickname() {
        return Optional.ofNullable(nickname);
    }

    public Optional<String> getEmail() {
        return Optional.ofNullable(email);
    }

    public Optional<String> getGithubLogin() {
        return Optional.ofNullable(githubLogin);
    }

    public Optional<String> getLinkedinId() {
        return Optional.ofNullable(linkedinId);
    }
}
