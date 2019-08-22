package com.sdehunt.commons.model;

import lombok.Data;

@Data
public class Task extends ShortTask {

    private String description;

    private String descriptionUrl;

    private String inputFilesUrl;

    private String requirements;

    private String job;

    private String jobUrl;
}
