package com.sdehunt.commons.model;

import java.time.Instant;

public interface User {

    /**
     * Identifier in our system.
     */
    String getId();

    /**
     * Setters
     */
    User setId(String id);

    /**
     * User's email.
     */
    String getEmail();

    User setEmail(String email);

    /**
     * User Github identifier.
     */
    String getGithub();

    User setGithub(String github);

    /**
     * User's LinkedIn identifier
     */
    String getLinkedIn();

    User setLinkedIn(String linkedIn);

    /**
     * When user was registered in our system.
     */
    Instant getCreated();

    User setCreated(Instant created);
}
