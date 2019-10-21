package com.sdehunt.commons.github.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CreateFileDTO {

    private String message;

    @JsonProperty("content")
    private String b64Content;

    private String branch;

}
