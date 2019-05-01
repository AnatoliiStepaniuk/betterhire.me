package com.sdehunt.commons;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Represents simple commit model (with the only fields needed).
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class SimpleCommit {
    private String sha;

    public String getSha() {
        return sha;
    }

    public SimpleCommit setSha(String sha) {
        this.sha = sha;
        return this;
    }
}
