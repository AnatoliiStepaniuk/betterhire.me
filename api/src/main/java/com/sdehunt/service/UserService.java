package com.sdehunt.service;

import com.sdehunt.commons.model.User;
import com.sdehunt.repository.UserRepository;

import java.util.Collections;
import java.util.Optional;

public class UserService {

    private final UserRepository userRepository;
    private final EmailService emailService;

    private final String TEMPLATE = "UpdateProfile";

    public UserService(UserRepository userRepository, EmailService emailService) {
        this.userRepository = userRepository;
        this.emailService = emailService;
    }

    public Optional<User> find(User user) {
        return userRepository.find(user);
    }

    public User update(User user) {
        return userRepository.update(user);
    }

    public User create(User user) {
        if (user.getEmail() != null) {
            emailService.send(user.getEmail(), TEMPLATE, Collections.emptyMap());
        }
        return userRepository.create(user);
    }

}
