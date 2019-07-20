package com.sdehunt.commons.github.exceptions;

import lombok.Data;

@Data
public class AlreadyInvitedException extends RuntimeException {

    private String repo;
    private String githubLogin;

    public AlreadyInvitedException(String repo, String githubLogin) {
        super(String.format("User %s was already invited to repository %s", githubLogin, repo));
        this.repo = repo;
        this.githubLogin = githubLogin;
    }
}
