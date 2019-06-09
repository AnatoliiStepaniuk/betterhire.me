package com.sdehunt.security;


import com.sdehunt.commons.model.User;
import com.sdehunt.exception.UserNotFoundException;
import com.sdehunt.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {
        User user = userRepository.byEmail(email)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User with email " + email + " is not found.")
                );

        return UserPrincipal.create(user);
    }

    public UserDetails loadUserById(String id) {
        User user = userRepository.get(id).orElseThrow(UserNotFoundException::new);

        return UserPrincipal.create(user);
    }
}