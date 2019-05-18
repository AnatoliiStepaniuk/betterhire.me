package com.sdehunt.dto;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class CreateUserDTO {
    private String email;
    private String github;
    private String linkedIn;
}
