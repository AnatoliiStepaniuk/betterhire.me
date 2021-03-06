package com.sdehunt.dto;

import com.sdehunt.commons.model.Language;
import com.sdehunt.commons.model.TaskType;
import lombok.Data;

import java.util.Set;

@Data
public class UpdateTaskDTO {
    private TaskType type;
    private String name;
    private String description;
    private String descriptionUrl;
    private String shortDescription;
    private String imageUrl;
    private String requirements;
    private String inputFilesUrl;
    private String company;
    private String city;
    private String job;
    private String jobUrl;
    private Set<String> tags;
    private Set<String> emails;
    private Set<Language> languages;
    private boolean enabled;
    private boolean submittable;
}
