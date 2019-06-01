package com.sdehunt.api;

import com.sdehunt.commons.TaskID;
import com.sdehunt.commons.model.Solution;
import com.sdehunt.commons.model.SolutionStatus;
import com.sdehunt.dto.SaveSolutionDTO;
import lombok.SneakyThrows;
import org.junit.Assert;
import org.junit.Test;

import java.util.UUID;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.equalToIgnoringCase;
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
        String id = host().contentType(APP_JSON)
                .body(solutionDTO)
                .post("/tasks/{taskId}/solutions/", taskId.name().toLowerCase())
                .body().asString();

        // Verify save (query by userId)
        host().get("/tasks/{taskId}/solutions?userId=" + userId, taskId).then()
                .body("size()", equalTo(1))
                .body("[0].taskId", equalTo(taskId.name()))
                .body("[0].userId", equalTo(userId))
                .body("[0].repo", equalTo(repo))
                .body("[0].commit", equalTo(commit));

        // Verify save (by id)
        host().get("/solutions/" + id)
                .then().log().ifValidationFails()
                .statusCode(SUCCESS)
                .body("id", equalTo(id))
                .body("taskId", equalToIgnoringCase(taskId.name()))
                .body("userId", equalTo(userId))
                .body("repo", equalTo(repo))
                .body("commit", equalTo(commit));

        // Verify save (by userId)
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
                .statusCode(NOT_FOUND);

        // Verify delete (query)
        host().get("/tasks/{taskId}/solutions?userId=" + userId, taskId.name().toLowerCase())
                .then().log().ifValidationFails()
                .statusCode(SUCCESS)
                .body("size()", is(0));
    }


    @Test
    public void invalidInputTest() {
        TaskID taskId = TaskID.SLIDES_TEST;
        String userId = UUID.randomUUID().toString();
        String repo = "AnatoliiStepaniuk/google_hash_code_2019";
        String commit = "master";
        String invalidRepo = "invalid_repo";
        String invalidCommit = "invalid_commit";

        // Verify invalid Repo response

        SaveSolutionDTO invalidRepoDTO = new SaveSolutionDTO()
                .setUserId(userId)
                .setRepo(invalidRepo)
                .setCommit(commit);

        String invalidRepoSolutionId = host().contentType(APP_JSON)
                .body(invalidRepoDTO)
                .post("/tasks/{taskId}/solutions/", taskId.name().toLowerCase())
                .body().asString();

        verifySolutionStatus(invalidRepoSolutionId, SolutionStatus.INVALID_FILES);


        // Verify invalid Commit response

        SaveSolutionDTO invalidCommitDTO = new SaveSolutionDTO()
                .setUserId(userId)
                .setRepo(repo)
                .setCommit(invalidCommit);

        String invalidCommitSolutionId = host().contentType(APP_JSON)
                .body(invalidCommitDTO)
                .post("/tasks/{taskId}/solutions/", taskId.name().toLowerCase())
                .body().asString();

        verifySolutionStatus(invalidCommitSolutionId, SolutionStatus.INVALID_FILES);

    }

    @SneakyThrows(InterruptedException.class)
    private void verifySolutionStatus(String solutionId, SolutionStatus status) {

        // Waiting for the status to change from initial
        SolutionStatus solutionStatus = SolutionStatus.IN_PROGRESS;
        while (solutionStatus == SolutionStatus.IN_PROGRESS) {
            solutionStatus = host().get("/solutions/{solutionId}", solutionId).as(Solution.class).getStatus();
            Thread.sleep(400);
        }

        Assert.assertEquals(status, solutionStatus);
    }

}
