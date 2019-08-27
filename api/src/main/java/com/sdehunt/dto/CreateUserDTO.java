package com.sdehunt.dto;

import lombok.Data;

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
    private boolean test;
}
