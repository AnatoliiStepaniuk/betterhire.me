package com.sdehunt;

import com.sdehunt.model.Task;
import com.sdehunt.model.impl.TaskImpl;
import io.restassured.specification.RequestSpecification;
import org.junit.Test;

import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.equalTo;


public class TaskApiTest {

    private final static String BASE_URI = "http://localhost";
    private final static int PORT = 8080;
    private final static String APP_JSON = "application/json";

    private final static String TASKS_PATH = "/tasks/";
    private final static int SUCCESS = 200;


    @Test
    public void getAllTasks() {

        Task task = new TaskImpl("task-description-" + UUID.randomUUID());

        // Saving task
        String id = host()
                .body(task)
                .contentType(APP_JSON)
                .post(TASKS_PATH)
                .asString();

        // Verify created
        host().contentType(APP_JSON)
                .get(TASKS_PATH + id)
                .then()
                .statusCode(SUCCESS)
                .body("description", equalTo(task.getDescription()));

        // Getting all tasks
        host().log().all()
                .get(TASKS_PATH)
                .then()
                .log().all()
                .statusCode(SUCCESS)
                .body("size()", is(1));

        // Deleting task
        host().delete(TASKS_PATH + id)
                .then()
                .statusCode(SUCCESS);

        // Verify deleted
        host().get(TASKS_PATH)
                .then()
                .statusCode(SUCCESS)
                .body("size()", is(0));
    }

    private RequestSpecification host() {
        return given().baseUri(BASE_URI).port(PORT);
    }

}
