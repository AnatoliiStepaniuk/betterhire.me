package com.sdehunt.dto;

import lombok.Data;

@Data
public class CreateUserDTO {
    private String email;
    private String githubLogin;
    private String linkedinId;
    private String nickname;
    private String imageUrl;
    private boolean test;
}
