package com.sdehunt.api;

import com.sdehunt.commons.TaskID;
import com.sdehunt.commons.model.Task;
import com.sdehunt.dto.UpdateTaskDTO;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.*;


public class TaskApiIT extends AbstractApiTest {

    private final static String TASKS_PATH = "/tasks/";

    @Test
    public void updateTaskTest() {

        TaskID taskId = TaskID.SLIDES_TEST;
        String description = UUID.randomUUID().toString();

        // Verify task is present
        host().contentType(APP_JSON)
                .get(TASKS_PATH + taskId.name().toLowerCase())
                .then()
                .log().ifValidationFails()
                .statusCode(SUCCESS)
                .body(not(isEmptyOrNullString()));

        // Updating task
        host()
                .body(new UpdateTaskDTO().setDescription(description))
                .contentType(APP_JSON)
                .put(TASKS_PATH + taskId.name().toLowerCase())
                .then()
                .statusCode(SUCCESS)
                .body(isEmptyString());

        // Verify updated
        host().contentType(APP_JSON)
                .get(TASKS_PATH + taskId.name().toLowerCase())
                .then()
                .log().ifValidationFails()
                .statusCode(SUCCESS)
                .body("id", equalToIgnoringCase(taskId.name()))
                .body("description", is(description));

        // Getting all tasks
        Task[] tasks = host().get(TASKS_PATH)
                .as(Task[].class);

        Task foundTask = Arrays.stream(tasks)
                .filter(t -> t.getId() == taskId)
                .findFirst()
                .orElseThrow();
        Assert.assertEquals(description, foundTask.getDescription());
    }
}
