package com.sdehunt.commons.model.impl;

import com.sdehunt.commons.model.User;

import java.time.Instant;
import java.util.Objects;

// TODO replace with lombok boilerplate like this
public class UserImpl implements User {

    private String id;
    private String email;
    private String github;
    private String linkedIn;
    private Instant created;

    public UserImpl() {
    }

    public UserImpl(User user) {
        this(user.getId(), user.getEmail(), user.getGithub(), user.getLinkedIn(), user.getCreated());
    }

    public UserImpl(String id, String email, String github, String linkedIn, Instant created) {
        this.id = id;
        this.email = email;
        this.github = github;
        this.linkedIn = linkedIn;
        this.created = created;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public String getGithub() {
        return github;
    }

    @Override
    public String getLinkedIn() {
        return linkedIn;
    }

    @Override
    public Instant getCreated() {
        return created;
    }

    public User setId(String id) {
        this.id = id;
        return this;
    }

    public User setEmail(String email) {
        this.email = email;
        return this;
    }

    public User setGithub(String github) {
        this.github = github;
        return this;
    }

    public User setLinkedIn(String linkedIn) {
        this.linkedIn = linkedIn;
        return this;
    }

    public User setCreated(Instant created) {
        this.created = created;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserImpl user = (UserImpl) o;
        return Objects.equals(id, user.id) &&
                Objects.equals(email, user.email) &&
                Objects.equals(github, user.github) &&
                Objects.equals(linkedIn, user.linkedIn) &&
                Objects.equals(created, user.created);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "UserImpl{" +
                "id='" + id + '\'' +
                ", email='" + email + '\'' +
                ", github='" + github + '\'' +
                ", linkedIn='" + linkedIn + '\'' +
                ", created=" + created +
                '}';
    }
}
