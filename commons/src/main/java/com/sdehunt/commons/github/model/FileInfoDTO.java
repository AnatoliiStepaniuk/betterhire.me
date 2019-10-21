package com.sdehunt.commons.github.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class FileInfoDTO {

    private String path;

    @JsonProperty("content")
    private String b64Content;

    private String sha;
}
