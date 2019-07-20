package com.sdehunt.commons.github.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class CreateHookDTO {

    private Config config;
    private List<String> events;

    @Data
    public static class Config {
        private String url;
        @JsonProperty("content_type")
        private String contentType;
        @JsonProperty("insecure_ssl")
        private String insecureSSL;
    }
}
