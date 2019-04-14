package com.sdehunt.api;

import com.sdehunt.commons.TaskID;
import com.sdehunt.commons.model.Solution;
import com.sdehunt.commons.model.impl.SolutionImpl;
import org.junit.Assert;
import org.junit.Test;

import java.util.UUID;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.Is.is;

public class SolutionApiTestIT extends AbstractApiTest {

    @Test
    public void crudTest() {
        TaskID taskId = TaskID.SLIDES;
        String userId = UUID.randomUUID().toString();
        String repo = "GRpro/google_hash_code_2019";
        String commit = "master";

        Solution solution = new SolutionImpl(
                null,
                TaskID.SLIDES,
                userId,
                repo,
                commit,
                0,
                null
        );

        // Saving solution
        long score = Long.valueOf(host()
                .contentType(APP_JSON)
                .body(solution)
                .post("/tasks/{taskId}/solutions/", taskId.name().toLowerCase())
                .asString());

        // Query
        SolutionImpl[] body = host().get("/tasks/{taskId}/solutions?userId = " + userId, taskId)
                .as(SolutionImpl[].class);
        Assert.assertEquals(1, body.length);
        Assert.assertEquals(taskId, body[0].getTaskId());
        Assert.assertEquals(score, body[0].getScore());
        Assert.assertEquals(userId, body[0].getUserId());
        Assert.assertEquals(repo, body[0].getRepo());
        Assert.assertEquals(commit, body[0].getCommit());
        String id = body[0].getId();

        // Verify save
        host().get("/solutions/" + id)
                .then().log().ifValidationFails()
                .statusCode(SUCCESS)
                .body("id", equalTo(id))
                .body("taskId", equalToIgnoringCase(taskId.name()))
                .body("userId", equalTo(userId))
                .body("repo", equalTo(repo))
                .body("commit", equalTo(commit));

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
        host().get("/tasks/{taskId}/solutions?userId = " + userId, taskId.name().toLowerCase())
                .then().log().ifValidationFails()
                .statusCode(SUCCESS)
                .body("size()", is(0));
    }

}
