package com.sdehunt.dto;

import com.sdehunt.commons.model.Language;
import lombok.Data;

import java.util.Set;

@Data
public class CreateUserDTO {
    private String email;
    private String name;
    private String githubLogin;
    private String linkedinId;
    private String nickname;
    private String imageUrl;
    private String cv;
    private String phone;
    private String city;
    private Set<Language> languages;
    private Boolean available;
    private boolean test;
    private Boolean manager;
}
