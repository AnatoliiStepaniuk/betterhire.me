package com.sdehunt.dto;

import lombok.Data;

@Data
public class UpdateTaskDTO {
    private String name;
    private String description;
    private String shortDescription;
    private String imageUrl;
    private boolean enabled; //TODO Boolean?
    private boolean submittable; //TODO Boolean?
}
