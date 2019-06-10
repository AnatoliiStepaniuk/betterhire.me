package com.sdehunt.api;

import com.sdehunt.commons.TaskID;
import com.sdehunt.commons.model.ShortTask;
import com.sdehunt.commons.model.Task;
import com.sdehunt.dto.UpdateTaskDTO;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.*;


public class TaskApiIT extends AbstractApiTest {

    private final static String TASKS = "/tasks";
    private final static String SHORT = "/short";

    @Test
    public void updateTaskTest() {

        TaskID taskId = TaskID.SLIDES_TEST;
        String description = UUID.randomUUID().toString();
        String shortDescription = UUID.randomUUID().toString();
        String name = UUID.randomUUID().toString();

        // Verify task is present
        host().contentType(APP_JSON)
                .get(TASKS + "/" + taskId.name().toLowerCase())
                .then()
                .log().ifValidationFails()
                .statusCode(SUCCESS)
                .body(not(isEmptyOrNullString()))
                .body("test", is(true));

        UpdateTaskDTO taskForUpdate = new UpdateTaskDTO()
                .setDescription(description)
                .setShortDescription(shortDescription)
                .setName(name);

        // Updating task
        host()
                .body(taskForUpdate)
                .contentType(APP_JSON)
                .put(TASKS + "/" + taskId.name().toLowerCase())
                .then()
                .statusCode(SUCCESS)
                .body(isEmptyString());

        // Verify updated
        host().contentType(APP_JSON)
                .get(TASKS + "/" + taskId.name().toLowerCase())
                .then()
                .log().ifValidationFails()
                .statusCode(SUCCESS)
                .body("id", equalToIgnoringCase(taskId.name()))
                .body("description", is(description))
                .body("shortDescription", is(shortDescription))
                .body("name", is(name))
                .body("test", is(true));

        // Verify updated
        host().contentType(APP_JSON)
                .get(TASKS + "/" + taskId.name().toLowerCase() + SHORT)
                .then()
                .log().ifValidationFails()
                .statusCode(SUCCESS)
                .body("id", equalToIgnoringCase(taskId.name()))
                .body("description", isEmptyOrNullString())
                .body("shortDescription", is(shortDescription))
                .body("name", is(name))
                .body("test", is(true));

        // Getting all tasks
        Task[] tasks = host().get(TASKS + "?test=true")
                .as(Task[].class);

        Task foundTask = Arrays.stream(tasks)
                .filter(t -> t.getId() == taskId)
                .findFirst()
                .orElseThrow();
        Assert.assertEquals(description, foundTask.getDescription());

        ShortTask[] shortTasks = host().get(TASKS + SHORT + "?test=true")
                .as(ShortTask[].class);
        ShortTask foundShortTask = Arrays.stream(shortTasks)
                .filter(t -> t.getId() == taskId)
                .findFirst()
                .orElseThrow();
        Assert.assertEquals(shortDescription, foundShortTask.getShortDescription());
    }
}
