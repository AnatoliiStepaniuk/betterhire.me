package com.sdehunt.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class PushHookDTO {

    private String before;

    private Repository repository;

    private List<Commit> commits;


    @Data
    public static class Repository {
        @JsonProperty("full_name")
        private String fullName;
    }

    @Data
    public static class Commit {
        private String id;
    }
}
