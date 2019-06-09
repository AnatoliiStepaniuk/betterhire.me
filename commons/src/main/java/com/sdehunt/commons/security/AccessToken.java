package com.sdehunt.commons.security;

import lombok.Data;

@Data
public class AccessToken {
    private String token;
    private OAuthProvider provider;
}
