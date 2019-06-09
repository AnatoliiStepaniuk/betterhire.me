package com.sdehunt.security;

import com.sdehunt.commons.model.User;

import java.util.Map;

public class GithubUserFactory {

    public static User getUser(Map<String, Object> attributes) {
        return new User()
                .setName((String) attributes.get("name"))
                .setEmail((String) attributes.get("email"))
                .setImageUrl((String) attributes.get("avatar_url"))
                .setGithubLogin((String) attributes.get("login"));
    }
}
