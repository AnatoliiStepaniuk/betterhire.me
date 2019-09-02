package com.sdehunt.commons.github.model;

import lombok.Data;

@Data
public class IssueDTO {

    private String id;

    private String title;

    private String body;

    private ShortUser user;

    private ShortUser assignee;

    @Data
    private static class ShortUser {
        private String login;
    }

}
