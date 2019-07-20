package com.sdehunt.commons.github.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CopyRepoDTO {

    private String owner;
    private String name;
    private String description;
    @JsonProperty("private")
    private boolean isPrivate;

    public CopyRepoDTO(String owner, String name, String description, boolean isPrivate) {
        this.owner = owner;
        this.name = name;
        this.description = description;
        this.isPrivate = isPrivate;
    }

    public CopyRepoDTO() {
    }
}
