package com.sdehunt.api;

import com.sdehunt.commons.TaskID;
import com.sdehunt.model.impl.TaskImpl;
import org.junit.Test;

import java.util.UUID;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.equalToIgnoringCase;
import static org.hamcrest.Matchers.isEmptyString;


public class TaskApiTestIT extends AbstractApiTest {

    private final static String TASKS_PATH = "/tasks/";

    @Test
    public void crudTest() {

        TaskID taskId = TaskID.SLIDES;
        String description = UUID.randomUUID().toString();

        // Saving task
        host()
                .body(new TaskImpl(taskId, description))
                .contentType(APP_JSON)
                .post(TASKS_PATH)
                .then()
                .statusCode(SUCCESS)
                .body(isEmptyString());

        // Verify created
        host().contentType(APP_JSON)
                .get(TASKS_PATH + taskId.name().toLowerCase())
                .then()
                .log().ifValidationFails()
                .statusCode(SUCCESS)
                .body("id", equalToIgnoringCase(taskId.name()))
                .body("description", is(description));

        // Getting all tasks
        host().get(TASKS_PATH)
                .then()
                .log().ifValidationFails()
                .statusCode(SUCCESS)
                .body("size()", is(1))
                .body("[0].id", equalToIgnoringCase(taskId.name()))
                .body("[0].description", is(description));

        // Deleting task
        host().delete(TASKS_PATH + taskId.name().toLowerCase())
                .then()
                .log().ifValidationFails()
                .statusCode(SUCCESS);

        // Verify created
        host().contentType(APP_JSON)
                .get(TASKS_PATH + taskId.name().toLowerCase())
                .then()
                .log().ifValidationFails()
                .statusCode(SUCCESS)
                .body(isEmptyString());

        // Verify deleted (get all)
        host().get(TASKS_PATH)
                .then()
                .statusCode(SUCCESS)
                .log().ifValidationFails()
                .body("size()", is(0));
    }
}
