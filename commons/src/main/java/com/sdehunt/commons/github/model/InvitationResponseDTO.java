package com.sdehunt.commons.github.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class InvitationResponseDTO {

    private Repository repository;

    @Data
    public static class Repository {
        @JsonProperty("html_url")
        private String htmlUrl;
    }
}
