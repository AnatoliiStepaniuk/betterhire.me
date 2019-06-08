package com.sdehunt.security;

import lombok.Data;

@Data
public class AccessToken {
    private String token;
    private OAuthProvider provider;
}
