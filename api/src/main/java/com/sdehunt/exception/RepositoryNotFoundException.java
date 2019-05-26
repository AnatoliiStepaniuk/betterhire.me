package com.sdehunt.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Repository not found")
public class RepositoryNotFoundException extends RuntimeException {

    public RepositoryNotFoundException() {
    }

    public RepositoryNotFoundException(String repo) {
        super("Repository " + repo + " not found");
    }
}
