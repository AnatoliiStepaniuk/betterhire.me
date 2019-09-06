package com.sdehunt.commons.model;

public enum TaskType {
    AUTO,
    MANUAL;

    public static TaskType of(String type) {
        return TaskType.valueOf(type.toUpperCase());
    }
}
