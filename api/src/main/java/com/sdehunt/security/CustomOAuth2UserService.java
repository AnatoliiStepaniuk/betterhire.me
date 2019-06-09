package com.sdehunt.security;

import com.sdehunt.commons.model.User;
import com.sdehunt.repository.AccessTokenRepository;
import com.sdehunt.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.http.OAuth2ErrorResponseErrorHandler;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AccessTokenRepository accessTokenRepository;

    public CustomOAuth2UserService() {
        super();
        supportAllMediaTypesForJacksonConverter();
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
        OAuthProvider provider = OAuthProvider.valueOf(oAuth2UserRequest.getClientRegistration().getRegistrationId().toUpperCase());
        String token = oAuth2UserRequest.getAccessToken().getTokenValue();
        User oAuthUser = OAuth2DataUserFactory.getUser(provider, attributes, token);
        Optional<User> found = userRepository.find(oAuthUser);
        User user;
        if (found.isPresent()) {
            oAuthUser.setId(found.get().getId());
            user = userRepository.update(oAuthUser);
        } else {
            user = userRepository.create(oAuthUser);
        }

        accessTokenRepository.save(user.getId(), provider, token);

        return UserPrincipal.create(user, attributes);
    }
}
