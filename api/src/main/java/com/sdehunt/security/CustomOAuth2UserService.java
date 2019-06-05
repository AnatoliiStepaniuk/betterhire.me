package com.sdehunt.security;

import com.sdehunt.commons.model.User;
import com.sdehunt.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.http.OAuth2ErrorResponseErrorHandler;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.*;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private static final ParameterizedTypeReference<Map<String, Object>> PARAMETERIZED_RESPONSE_TYPE =
            new ParameterizedTypeReference<Map<String, Object>>() {
            };
    @Autowired
    private UserRepository userRepository;
    private RestTemplate restTemplate;

    public CustomOAuth2UserService() {
        super();
        supportAllMediaTypesForJacksonConverter();
        this.restTemplate = new RestTemplate();
    }

    private void supportAllMediaTypesForJacksonConverter() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setErrorHandler(new OAuth2ErrorResponseErrorHandler());
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setSupportedMediaTypes(Collections.singletonList(MediaType.ALL));
        restTemplate.getMessageConverters().add(converter);
        super.setRestOperations(restTemplate);
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(oAuth2UserRequest);

        try {
            return processOAuth2User(oAuth2UserRequest, oAuth2User);
        } catch (AuthenticationException ex) {
            throw ex;
        } catch (Exception ex) {
            // Throwing an instance of AuthenticationException will trigger the OAuth2AuthenticationFailureHandler
            throw new InternalAuthenticationServiceException(ex.getMessage(), ex.getCause());
        }
    }

    private OAuth2User processOAuth2User(OAuth2UserRequest oAuth2UserRequest, OAuth2User oAuth2User) {
        Map<String, Object> attributes = new HashMap<>(oAuth2User.getAttributes());
        if (oAuth2UserRequest.getClientRegistration().getRegistrationId().equalsIgnoreCase(AuthProvider.linkedin.toString())) {
            populateLinkedinData(oAuth2UserRequest, attributes);
        }

        OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(oAuth2UserRequest.getClientRegistration().getRegistrationId(), attributes);

        if (StringUtils.isEmpty(oAuth2UserInfo.getEmail())) {
            throw new RuntimeException("Email not found from OAuth2 provider");
        }

        Optional<User> found = userRepository.byEmail(oAuth2UserInfo.getEmail());
        User user;
        if (found.isPresent()) {
            user = updateExistingUser(found.get(), oAuth2UserInfo);
        } else {
            user = registerNewUser(oAuth2UserRequest, oAuth2UserInfo);
        }

        return UserPrincipal.create(user, attributes);
    }

    private User registerNewUser(OAuth2UserRequest oAuth2UserRequest, OAuth2UserInfo oAuth2UserInfo) {
        User user = new User();

//        user.setProvider(AuthProvider.valueOf(oAuth2UserRequest.getClientRegistration().getRegistrationId()));
//        user.setProviderId(oAuth2UserInfo.getId()); // TODO
        user.setName(oAuth2UserInfo.getName());
        user.setEmail(oAuth2UserInfo.getEmail());
        user.setImageUrl(oAuth2UserInfo.getImageUrl());
        return userRepository.create(user);
    }

    private User updateExistingUser(User existingUser, OAuth2UserInfo oAuth2UserInfo) { // TODO rewrite?
        existingUser.setName(oAuth2UserInfo.getName());
        existingUser.setImageUrl(oAuth2UserInfo.getImageUrl());
        return userRepository.update(existingUser);
    }

    private void populateLinkedinData(OAuth2UserRequest oAuth2UserRequest, Map<String, Object> attributes) {
        attributes.put("email", getLinkedInEmail(oAuth2UserRequest));
        attributes.put("profilePicture", getLinkedinProfileImage(oAuth2UserRequest));
    }

    private String getLinkedInEmail(OAuth2UserRequest oAuth2UserRequest) {
        URI uri = URI.create("https://api.linkedin.com/v2/clientAwareMemberHandles?q=members&projection=(elements*(primary,type,handle~))");

        RequestEntity<Object> request = new RequestEntity<>(getHeaders(oAuth2UserRequest), HttpMethod.GET, uri);
        ResponseEntity<Map<String, Object>> response = restTemplate.exchange(request, PARAMETERIZED_RESPONSE_TYPE);
        String email = (String) ((Map) ((Map) ((List) response.getBody().get("elements")).get(0)).get("handle~")).get("emailAddress");
        return email;
    }

    private String getLinkedinProfileImage(OAuth2UserRequest oAuth2UserRequest) {
        URI uri = URI.create("https://api.linkedin.com/v2/me?projection=(id,profilePicture(displayImage~:playableStreams))");

        RequestEntity<Object> request = new RequestEntity<>(getHeaders(oAuth2UserRequest), HttpMethod.GET, uri);
        ResponseEntity<Map<String, Object>> response = restTemplate.exchange(request, PARAMETERIZED_RESPONSE_TYPE);
        List elements = (List) ((Map) ((Map) response.getBody().get("profilePicture")).get("displayImage~")).get("elements");
        String profileImage = (String) ((Map) ((List) ((Map) elements.get(elements.size() - 1)).get("identifiers")).get(0)).get("identifier");
        return profileImage;
    }

    private HttpHeaders getHeaders(OAuth2UserRequest oAuth2UserRequest) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.set(HttpHeaders.AUTHORIZATION, "Bearer " + oAuth2UserRequest.getAccessToken().getTokenValue());
        return headers;
    }

}
