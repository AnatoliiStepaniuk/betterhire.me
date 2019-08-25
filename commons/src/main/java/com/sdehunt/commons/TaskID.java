package com.sdehunt.commons;

/**
 * Generally used identifier of task
 */
public enum TaskID {

    SLIDES,
    CARS,
    BALLOONS,
    DATACENTER,
    PIZZA,
    LETTERS,
    CITIES,
    LETTER,
    CITY,
    SLIDES_TEST;

    public static TaskID of(String taskId) {
        return TaskID.valueOf(taskId.toUpperCase());
    }

}
