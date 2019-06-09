package com.sdehunt.commons.repo;

import com.sdehunt.commons.security.AccessToken;
import com.sdehunt.commons.security.OAuthProvider;

import java.util.Optional;

public interface AccessTokenRepository {

    void save(String userId, OAuthProvider provider, String token);

    Optional<AccessToken> find(String userId, OAuthProvider provider);
}
