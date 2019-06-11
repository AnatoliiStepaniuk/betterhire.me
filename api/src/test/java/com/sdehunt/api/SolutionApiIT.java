package com.sdehunt.api;

import com.sdehunt.commons.TaskID;
import com.sdehunt.commons.model.Solution;
import com.sdehunt.commons.model.SolutionStatus;
import com.sdehunt.dto.CreateUserDTO;
import com.sdehunt.dto.SaveSolutionDTO;
import com.sdehunt.dto.SolutionIdDTO;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.restassured.http.Header;
import io.restassured.response.Response;
import lombok.SneakyThrows;
import org.junit.Assert;
import org.junit.Test;

import java.util.Date;
import java.util.UUID;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.equalToIgnoringCase;
import static org.hamcrest.core.Is.is;

public class SolutionApiIT extends AbstractApiTest {

    @Test
    public void crudTest() {
        TaskID taskId = TaskID.SLIDES_TEST;
        CreateUserDTO createUserDTO = new CreateUserDTO()
                .setEmail(UUID.randomUUID().toString() + "@gmail.com")
                .setNickname("NN" + UUID.randomUUID().toString())
                .setTest(true);
        String userId = "976db4b4-c82c-4971-9a47-32835bfc6690"; // sdehuntdeveloper userId // TODO can unhardcode it when get add route for getting user by it's github login
        String repo = "sdehuntdeveloper/google_hash_code_2019_public";
        String commit = "61f487523ad641cc6fffc44ded7537d94cf0d1eb";

        String jwt = Jwts.builder()
                .setSubject(userId)
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + 864000000))
                .signWith(SignatureAlgorithm.HS512, "926D96C90030DD58429D2751AC1BDBBC")
                .compact();
        SaveSolutionDTO solutionDTO = new SaveSolutionDTO()
                .setRepo(repo)
                .setCommit(commit)
                .setTest(true);

        // Saving solution
        String id = host().contentType(APP_JSON)
                .header(new Header("Authorization", "Bearer " + jwt))
                .body(solutionDTO)
                .post("/tasks/{taskId}/solutions/", taskId.name().toLowerCase())
                .as(SolutionIdDTO.class).getId();

        verifySolutionStatus(id, SolutionStatus.ACCEPTED);

        // Verify save (query by userId)
        host().get("/tasks/{taskId}/solutions?userId=" + userId + "&status=ACCEPTED&test=true", taskId).then()
                .body("size()", equalTo(1))
                .body("[0].taskId", equalTo(taskId.name()))
                .body("[0].userId", equalTo(userId))
                .body("[0].repo", equalTo(repo))
                .body("[0].commit", equalTo(commit))
                .body("[0].status", equalTo(SolutionStatus.ACCEPTED.name()))
                .body("[0].test", equalTo(true));

        // Verify save (by id)
        host().get("/solutions/" + id)
                .then().log().ifValidationFails()
                .statusCode(SUCCESS)
                .body("id", equalTo(id))
                .body("taskId", equalToIgnoringCase(taskId.name()))
                .body("userId", equalTo(userId))
                .body("repo", equalTo(repo))
                .body("commit", equalTo(commit))
                .body("status", equalTo(SolutionStatus.ACCEPTED.name()));

        // Verify save (by userId)
        host().get("/users/{userId}/solutions?test=true", userId)
                .then().log().ifValidationFails()
                .statusCode(SUCCESS)
                .body("size()", equalTo(1))
                .body("[0].id", equalTo(id))
                .body("[0].taskId", equalToIgnoringCase(taskId.name()))
                .body("[0].userId", equalTo(userId))
                .body("[0].repo", equalTo(repo))
                .body("[0].commit", equalTo(commit))
                .body("[0].status", equalTo(SolutionStatus.ACCEPTED.name()));

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
        CreateUserDTO createUserDTO = new CreateUserDTO()
                .setEmail(UUID.randomUUID().toString() + "@gmail.com")
                .setNickname("NN" + UUID.randomUUID().toString())
                .setTest(true);
        String userId = "976db4b4-c82c-4971-9a47-32835bfc6690"; // sdehuntdeveloper userId // TODO can unhardcode it when get add route for getting user by it's github login

        String repo = "sdehuntdeveloper/google_hash_code_2019_public";
        String commit = "master";
        String invalidRepo = "invalid_repo";
        String invalidCommit = "invalid_commit";

        String jwt = Jwts.builder()
                .setSubject(userId)
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + 864000000))
                .signWith(SignatureAlgorithm.HS512, "926D96C90030DD58429D2751AC1BDBBC")
                .compact();

        // First verify successful response for valid request
        SaveSolutionDTO validDTO = new SaveSolutionDTO()
                .setRepo(repo)
                .setCommit(commit)
                .setTest(true);

        Response submitResponse = host().contentType(APP_JSON)
                .header(new Header("Authorization", "Bearer " + jwt))
                .body(validDTO)
                .post("/tasks/{taskId}/solutions/", taskId.name().toLowerCase());
        submitResponse.then().statusCode(is(SUCCESS));
        String solutionId = submitResponse.as(SolutionIdDTO.class).getId();
        // Delete
        host().delete("/solutions/{id}", solutionId)
                .then().log().ifValidationFails()
                .statusCode(SUCCESS);

        // Verify invalid Repo response
        SaveSolutionDTO invalidRepoDTO = new SaveSolutionDTO()
                .setRepo(invalidRepo)
                .setCommit(commit)
                .setTest(true);
        host().contentType(APP_JSON)
                .header(new Header("Authorization", "Bearer " + jwt))
                .body(invalidRepoDTO)
                .post("/tasks/{taskId}/solutions/", taskId.name().toLowerCase())
                .then().statusCode(is(NOT_FOUND));

        // Verify invalid Commit response
        SaveSolutionDTO invalidCommitDTO = new SaveSolutionDTO()
                .setRepo(repo)
                .setCommit(invalidCommit)
                .setTest(true);
        host().contentType(APP_JSON)
                .header(new Header("Authorization", "Bearer " + jwt))
                .body(invalidCommitDTO)
                .post("/tasks/{taskId}/solutions/", taskId.name().toLowerCase())
                .then().statusCode(is(NOT_FOUND));

        // Verify invalid User response

        String invalidUserJwt = Jwts.builder()
                .setSubject(UUID.randomUUID().toString())
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + 864000000))
                .signWith(SignatureAlgorithm.HS512, "926D96C90030DD58429D2751AC1BDBBC")
                .compact();

        SaveSolutionDTO invalidSolutionDTO = new SaveSolutionDTO()
                .setRepo(repo)
                .setCommit(commit)
                .setTest(true);
        host().contentType(APP_JSON)
                .header(new Header("Authorization", "Bearer " + invalidUserJwt))
                .body(invalidSolutionDTO)
                .post("/tasks/{taskId}/solutions/", taskId.name().toLowerCase())
                .then().statusCode(is(NOT_FOUND));
    }

    @Test
    public void invalidSolutionTest() {
        TaskID taskId = TaskID.SLIDES_TEST;
        CreateUserDTO createUserDTO = new CreateUserDTO()
                .setEmail(UUID.randomUUID().toString() + "@gmail.com")
                .setNickname("NN" + UUID.randomUUID().toString())
                .setTest(true);
        String userId = "976db4b4-c82c-4971-9a47-32835bfc6690"; // sdehuntdeveloper userId // TODO can unhardcode it when get add route for getting user by it's github login
        String invalidSolutionRepo = "sdehuntdeveloper/google_hash_code_2019_invalid";
        String commit = "master";

        String jwt = Jwts.builder()
                .setSubject(userId)
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + 864000000))
                .signWith(SignatureAlgorithm.HS512, "926D96C90030DD58429D2751AC1BDBBC")
                .compact();

        SaveSolutionDTO invalidSolutionDTO = new SaveSolutionDTO()
                .setRepo(invalidSolutionRepo)
                .setCommit(commit)
                .setTest(true);

        String invalidSolutionId = host().contentType(APP_JSON)
                .header(new Header("Authorization", "Bearer " + jwt))
                .body(invalidSolutionDTO)
                .post("/tasks/{taskId}/solutions/", taskId.name().toLowerCase())
                .as(SolutionIdDTO.class).getId();

        verifySolutionStatus(invalidSolutionId, SolutionStatus.INVALID_SOLUTION);
        // Delete
        host().delete("/solutions/{id}", invalidSolutionId)
                .then().log().ifValidationFails()
                .statusCode(SUCCESS);
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
