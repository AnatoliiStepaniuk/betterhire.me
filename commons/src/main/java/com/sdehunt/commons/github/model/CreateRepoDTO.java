package com.sdehunt.commons.github.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CreateRepoDTO {

    private String name;

    @JsonProperty("private")
    private boolean isPrivate;

    @JsonProperty("is_template")
    private boolean isTemplate;

}
