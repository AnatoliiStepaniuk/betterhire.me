package com.sdehunt.api;

import com.sdehunt.commons.TaskID;
import com.sdehunt.dto.SaveSolutionDTO;
import com.sdehunt.dto.SolutionScoreDTO;
import org.junit.Test;

import java.util.UUID;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.Is.is;

public class SolutionApiIT extends AbstractApiTest {

    @Test
    public void crudTest() {
        TaskID taskId = TaskID.SLIDES_TEST;
        String userId = UUID.randomUUID().toString();
        String repo = "AnatoliiStepaniuk/google_hash_code_2019";
        String commit = "61f487523ad641cc6fffc44ded7537d94cf0d1eb";

        SaveSolutionDTO solutionDTO = new SaveSolutionDTO()
                .setUserId(userId)
                .setRepo(repo)
                .setCommit(commit);

        // Saving solution
        SolutionScoreDTO response = host().contentType(APP_JSON)
                .body(solutionDTO)
                .post("/tasks/{taskId}/solutions/", taskId.name().toLowerCase())
                .as(SolutionScoreDTO.class);

        String id = response.getSolutionId();

        // Query
        host().get("/tasks/{taskId}/solutions?userId=" + userId, taskId).then()
                .body("size()", equalTo(1))
                .body("[0].taskId", equalTo(taskId.name()))
                .body("[0].score", equalTo((int) response.getScore()))
                .body("[0].userId", equalTo(userId))
                .body("[0].repo", equalTo(repo))
                .body("[0].commit", equalTo(commit));

        // Verify save
        host().get("/solutions/" + id)
                .then().log().ifValidationFails()
                .statusCode(SUCCESS)
                .body("id", equalTo(id))
                .body("taskId", equalToIgnoringCase(taskId.name()))
                .body("userId", equalTo(userId))
                .body("repo", equalTo(repo))
                .body("commit", equalTo(commit));

        host().get("/users/{userId}/solutions", userId)
                .then().log().ifValidationFails()
                .statusCode(SUCCESS)
                .body("size()", equalTo(1))
                .body("[0].id", equalTo(id))
                .body("[0].taskId", equalToIgnoringCase(taskId.name()))
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
        host().get("/tasks/{taskId}/solutions?userId=" + userId, taskId.name().toLowerCase())
                .then().log().ifValidationFails()
                .statusCode(SUCCESS)
                .body("size()", is(0));
    }

}
