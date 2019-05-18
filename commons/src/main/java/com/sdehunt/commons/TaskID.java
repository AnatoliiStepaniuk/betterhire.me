package com.sdehunt.commons;

/**
 * Generally used identifier of task
 */
public enum TaskID {

    SLIDES;

    public static TaskID of(String taskId) {
        return TaskID.valueOf(taskId.toUpperCase());
    }

}
