package com.sdehunt.security;

import com.sdehunt.commons.model.User;
import com.sdehunt.commons.security.OAuthProvider;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

public class OAuth2DataUserFactory {

    private static final ParameterizedTypeReference<Map<String, Object>> PARAMETERIZED_RESPONSE_TYPE =
            new ParameterizedTypeReference<Map<String, Object>>() {
            };
    private static RestTemplate restTemplate = new RestTemplate();

    public static User getUser(OAuthProvider provider, Map<String, Object> attributes, String token) {
        switch (provider) {
            case GITHUB:
                return GithubUserFactory.getUser(attributes);
            case LINKEDIN:
                return LinkedinUserFactory.getUser(attributes, token);
            default:
                throw new RuntimeException("Sorry! Login with " + provider + " is not supported yet.");
        }
    }
}
