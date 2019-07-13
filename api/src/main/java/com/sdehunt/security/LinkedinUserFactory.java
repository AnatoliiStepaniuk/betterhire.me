package com.sdehunt.security;

import com.sdehunt.commons.model.User;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class LinkedinUserFactory {

    private static final ParameterizedTypeReference<Map<String, Object>> PARAMETERIZED_RESPONSE_TYPE =
            new ParameterizedTypeReference<>() {
            };
    private static RestTemplate restTemplate = new RestTemplate();

    public static User getUser(Map<String, Object> attributes, String token) {
        return new User()
                .setName((String) attributes.get("localizedFirstName") + " " + attributes.get("localizedLastName"))
                .setEmail(getEmail(token))
                .setImageUrl(getProfileImage(token)) // TODO can get also login?
                .setLinkedinId(attributes.get("id").toString());
    }

    private static String getEmail(String token) {
        URI uri = URI.create("https://api.linkedin.com/v2/clientAwareMemberHandles?q=members&projection=(elements*(primary,type,handle~))");

        RequestEntity<Object> request = new RequestEntity<>(headers(token), HttpMethod.GET, uri);
        ResponseEntity<Map<String, Object>> response = restTemplate.exchange(request, PARAMETERIZED_RESPONSE_TYPE);
        String email = (String) ((Map) ((Map) ((List) response.getBody().get("elements")).get(0)).get("handle~")).get("emailAddress");
        return email;
    }

    private static String getProfileImage(String token) {
        URI uri = URI.create("https://api.linkedin.com/v2/me?projection=(id,profilePicture(displayImage~:playableStreams))");

        RequestEntity<Object> request = new RequestEntity<>(headers(token), HttpMethod.GET, uri);
        ResponseEntity<Map<String, Object>> response = restTemplate.exchange(request, PARAMETERIZED_RESPONSE_TYPE);
        List elements = (List) ((Map) ((Map) response.getBody().get("profilePicture")).get("displayImage~")).get("elements");
        String profileImage = (String) ((Map) ((List) ((Map) elements.get(elements.size() - 1)).get("identifiers")).get(0)).get("identifier");
        return profileImage;
    }

    private static HttpHeaders headers(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.set(HttpHeaders.AUTHORIZATION, "Bearer " + token);
        return headers;
    }

}
