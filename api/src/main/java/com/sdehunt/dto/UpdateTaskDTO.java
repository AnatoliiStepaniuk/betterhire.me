package com.sdehunt.dto;

import lombok.Data;

@Data
public class UpdateTaskDTO {
    private String name;
    private String description;
    private String descriptionUrl;
    private String shortDescription;
    private String imageUrl;
    private String requirements;
    private boolean enabled;
    private boolean submittable;
}
