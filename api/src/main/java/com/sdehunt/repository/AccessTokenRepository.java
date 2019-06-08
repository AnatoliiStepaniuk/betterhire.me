package com.sdehunt.repository;

import com.sdehunt.security.AccessToken;
import com.sdehunt.security.OAuthProvider;

import java.util.Optional;

public interface AccessTokenRepository {

    void save(String userId, OAuthProvider provider, String token);

    Optional<AccessToken> find(String userId, OAuthProvider provider);
}
