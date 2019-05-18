package com.sdehunt.commons.model;

import java.time.Instant;

public interface User {

    /**
     * Identifier in our system.
     */
    String getId();

    /**
     * User's email.
     */
    String getEmail();

    /**
     * User Github identifier.
     */
    String getGithub();

    /**
     * User's LinkedIn identifier
     */
    String getLinkedIn();

    /**
     * When user was registered in our system.
     */
    Instant getCreated();

    /**
     * When user was last updated in our system.
     */
    Instant getUpdated();

    User setUpdated(Instant created);

    /*
     * Setters
     */
    User setId(String id);

    User setEmail(String email);

    User setGithub(String github);

    User setCreated(Instant created);

    User setLinkedIn(String linkedIn);

}
