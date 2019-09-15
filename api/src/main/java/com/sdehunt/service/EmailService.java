package com.sdehunt.service;

import com.sdehunt.commons.email.EmailSender;
import com.sdehunt.commons.model.User;
import com.sdehunt.repository.UserRepository;

import java.util.Collections;
import java.util.Map;

public class EmailService {

    private final String FROM = "BetterHire.me <hey@betterhire.me>";

    private final UserRepository userRepository;

    private final EmailSender emailSender;

    public EmailService(UserRepository userRepository, EmailSender emailSender) {
        this.userRepository = userRepository;
        this.emailSender = emailSender;
    }

    public void sendUsersBySql(String templateId, String sql) {
        sendUsersBySql(templateId, sql, Collections.emptyMap());
    }

    public void sendUsersBySql(String templateId, String sql, Map<String, Object> params) {
        userRepository.getBySql(sql).stream()
                .map(User::getEmail)
                .filter(this::isNonEmpty)
                .forEach(e -> emailSender.send(FROM, e, templateId, params));
    }

    private boolean isNonEmpty(String s) {
        return s != null && !s.isEmpty() && !s.isBlank();
    }
}
