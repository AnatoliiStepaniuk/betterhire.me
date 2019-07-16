package com.sdehunt.dto;

import com.sdehunt.commons.model.Tag;
import lombok.Data;

import java.util.Set;

@Data
public class UpdateTaskDTO {
    private String name;
    private String description;
    private String descriptionUrl;
    private String shortDescription;
    private String imageUrl;
    private String requirements;
    private String inputFilesUrl;
    private Set<Tag> tags;
    private boolean enabled;
    private boolean submittable;
}
