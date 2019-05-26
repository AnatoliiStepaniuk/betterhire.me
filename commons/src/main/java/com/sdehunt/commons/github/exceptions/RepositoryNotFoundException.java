package com.sdehunt.commons.github.exceptions;

public class RepositoryNotFoundException extends Exception {

    public RepositoryNotFoundException() {
    }

    public RepositoryNotFoundException(String repo) {
        super("Repository " + repo + " not found");
    }
}
