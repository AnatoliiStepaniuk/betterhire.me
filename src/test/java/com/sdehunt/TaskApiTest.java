package com.sdehunt;

import org.junit.Test;

import static io.restassured.RestAssured.get;
import static org.hamcrest.Matchers.is;


public class TaskApiTest {

    @Test
    public void getAllTasks() {
        // TODO verify size is 2
        get("/tasks")
                .then()
                .body("size()", is(2));
    }

}
