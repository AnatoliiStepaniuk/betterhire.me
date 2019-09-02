package com.sdehunt.commons.github.model;

import lombok.Data;

import java.util.Set;

@Data
public class CreateIssueDTO {

    private String title;

    private String body;

    private Set<String> assignees;

}
