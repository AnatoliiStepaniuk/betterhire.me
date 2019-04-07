package com.sdehunt.api;

import com.sdehunt.model.Solution;
import com.sdehunt.model.impl.SolutionImpl;
import org.junit.Test;

import java.util.Random;
import java.util.UUID;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.isEmptyString;
import static org.hamcrest.core.Is.is;

public class SolutionApiTestIT extends AbstractApiTest {

    @Test
    public void crudTest() {
        String taskId = UUID.randomUUID().toString();
        String userId = UUID.randomUUID().toString();
        String repo = UUID.randomUUID().toString();
        String commit = UUID.randomUUID().toString().substring(0, 7);
        long score = Math.abs(new Random().nextLong());

        Solution solution = new SolutionImpl(
                null,
                taskId,
                userId,
                repo,
                commit,
                score,
                null
        );

        // Saving solution
        String id = host()
                .contentType(APP_JSON)
                .body(solution)
                .post("/tasks/{taskId}/solutions/", taskId)
                .asString();

        // Verify save
        host().get("/solutions/" + id)
                .then().log().ifValidationFails()
                .statusCode(SUCCESS)
                .body("id", equalTo(id))
                .body("taskId", equalTo(taskId))
                .body("userId", equalTo(userId))
                .body("repo", equalTo(repo))
                .body("commit", equalTo(commit));

        // Query
        host().get("/tasks/{taskId}/solutions?userId = " + userId, taskId)
                .then().log().ifValidationFails()
                .statusCode(SUCCESS)
                .body("size()", is(1))
                .body("[0].taskId", equalTo(taskId))
                .body("[0].userId", equalTo(userId))
                .body("[0].repo", equalTo(repo))
                .body("[0].commit", equalTo(commit));

        // Delete
        host().delete("/solutions/{id}", id)
                .then().log().ifValidationFails()
                .statusCode(SUCCESS);

        // Verify delete
        host().get("/solutions/" + id)
                .then().log().ifValidationFails()
                .statusCode(SUCCESS)
                .body(isEmptyString());

        // Verify delete (query)
        host().get("/tasks/{taskId}/solutions?userId = " + userId, taskId)
                .then().log().ifValidationFails()
                .statusCode(SUCCESS)
                .body("size()", is(0));
    }

}
