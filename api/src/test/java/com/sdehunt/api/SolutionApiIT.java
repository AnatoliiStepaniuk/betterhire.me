package com.sdehunt.api;

import com.sdehunt.commons.TaskID;
import com.sdehunt.commons.model.Solution;
import com.sdehunt.commons.model.SolutionStatus;
import com.sdehunt.commons.model.User;
import com.sdehunt.dto.CreateUserDTO;
import com.sdehunt.dto.SaveSolutionDTO;
import com.sdehunt.dto.SolutionIdDTO;
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
        CreateUserDTO createUserDTO = new CreateUserDTO().setEmail(UUID.randomUUID().toString() + "@gmail.com");
        String userId = host().contentType(APP_JSON).body(createUserDTO).post("/users").as(User.class).getId();
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
                .as(SolutionIdDTO.class).getId();

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
        CreateUserDTO createUserDTO = new CreateUserDTO().setEmail(UUID.randomUUID().toString() + "@gmail.com");
        String userId = host().contentType(APP_JSON).body(createUserDTO).post("/users").as(User.class).getId();

        String repo = "AnatoliiStepaniuk/google_hash_code_2019";
        String commit = "master";
        String invalidUserId = UUID.randomUUID().toString();
        String invalidRepo = "invalid_repo";
        String invalidCommit = "invalid_commit";

        // First verify successful response for valid request
        SaveSolutionDTO validDTO = new SaveSolutionDTO()
                .setUserId(userId)
                .setRepo(repo)
                .setCommit(commit);
        host().contentType(APP_JSON)
                .body(validDTO)
                .post("/tasks/{taskId}/solutions/", taskId.name().toLowerCase())
                .then().statusCode(is(SUCCESS));

        // Verify invalid Repo response
        SaveSolutionDTO invalidRepoDTO = new SaveSolutionDTO()
                .setUserId(userId)
                .setRepo(invalidRepo)
                .setCommit(commit);
        host().contentType(APP_JSON)
                .body(invalidRepoDTO)
                .post("/tasks/{taskId}/solutions/", taskId.name().toLowerCase())
                .then().statusCode(is(NOT_FOUND));

        // Verify invalid Commit response
        SaveSolutionDTO invalidCommitDTO = new SaveSolutionDTO()
                .setUserId(userId)
                .setRepo(repo)
                .setCommit(invalidCommit);
        host().contentType(APP_JSON)
                .body(invalidCommitDTO)
                .post("/tasks/{taskId}/solutions/", taskId.name().toLowerCase())
                .then().statusCode(is(NOT_FOUND));

        // Verify invalid User response
        SaveSolutionDTO invalidSolutionDTO = new SaveSolutionDTO()
                .setUserId(invalidUserId)
                .setRepo(repo)
                .setCommit(commit);
        host().contentType(APP_JSON)
                .body(invalidSolutionDTO)
                .post("/tasks/{taskId}/solutions/", taskId.name().toLowerCase())
                .then().statusCode(is(NOT_FOUND));
    }

    @Test
    public void invalidSolutionTest() {
        TaskID taskId = TaskID.SLIDES_TEST;
        CreateUserDTO createUserDTO = new CreateUserDTO().setEmail(UUID.randomUUID().toString() + "@gmail.com");
        String userId = host().contentType(APP_JSON).body(createUserDTO).post("/users").as(User.class).getId();
        String invalidSolutionRepo = "AnatoliiStepaniuk/google_hash_code_2019_invalid";
        String commit = "master";

        SaveSolutionDTO invalidSolutionDTO = new SaveSolutionDTO()
                .setUserId(userId)
                .setRepo(invalidSolutionRepo)
                .setCommit(commit);

        String invalidSolutionId = host().contentType(APP_JSON)
                .body(invalidSolutionDTO)
                .post("/tasks/{taskId}/solutions/", taskId.name().toLowerCase())
                .as(SolutionIdDTO.class).getId();

        verifySolutionStatus(invalidSolutionId, SolutionStatus.INVALID_SOLUTION);
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
