package com.sdehunt.api;

import com.sdehunt.commons.model.User;
import com.sdehunt.dto.CreateUserDTO;
import io.restassured.response.Response;
import org.junit.Assert;
import org.junit.Test;

import java.util.Collection;
import java.util.UUID;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.Is.is;

public class UserApiIT extends AbstractApiTest {

    @Test
    public void crudTest() {
        String email = UUID.randomUUID().toString() + "@gmail.com";
        String githubLogin = "GH" + UUID.randomUUID().toString();
        String linkedInId = "LI" + UUID.randomUUID().toString();
        String nickname = "NN" + UUID.randomUUID().toString();
        String imageUrl = "IMG" + UUID.randomUUID().toString();

        int usersCountBefore = host().get("/users").as(Collection.class).size();

        CreateUserDTO createRequest = new CreateUserDTO()
                .setEmail(email)
                .setGithubLogin(githubLogin)
                .setLinkedinId(linkedInId)
                .setNickname(nickname)
                .setImageUrl(imageUrl);

        Response response = host()
                .contentType(APP_JSON)
                .body(createRequest)
                .post("/users");

        response.then().log().ifValidationFails()
                .statusCode(SUCCESS)
                .body("email", is(email))
                .body("githubLogin", is(githubLogin))
                .body("linkedinId", is(linkedInId))
                .body("nickname", is(nickname))
                .body("imageUrl", is(imageUrl));

        User user = response.as(User.class);

        host().get("/users/{userId}", user.getId())
                .then().log().ifValidationFails()
                .body("id", is(user.getId()))
                .body("email", is(user.getEmail()))
                .body("githubLogin", is(user.getGithubLogin()))
                .body("linkedinId", is(user.getLinkedinId()))
                .body("nickname", is(user.getNickname()))
                .body("imageUrl", is(user.getImageUrl()))
                .body("created", notNullValue())
                .body("updated", notNullValue());

        int usersCountAfter = host().get("/users").as(Collection.class).size();
        Assert.assertEquals(usersCountBefore + 1, usersCountAfter);

        CreateUserDTO updateRequest = new CreateUserDTO()
                .setEmail(user.getEmail() + "2")
                .setGithubLogin(user.getGithubLogin() + "2")
                .setLinkedinId(user.getLinkedinId() + "2")
                .setNickname(user.getNickname() + "2")
                .setImageUrl(user.getImageUrl() + "2");

        host().contentType(APP_JSON)
                .body(updateRequest)
                .put("/users/{userId}", user.getId())
                .then().log().ifValidationFails()
                .statusCode(SUCCESS)
                .body("id", is(user.getId()))
                .body("email", is(updateRequest.getEmail()))
                .body("githubLogin", is(updateRequest.getGithubLogin()))
                .body("linkedinId", is(updateRequest.getLinkedinId()))
                .body("nickname", is(updateRequest.getNickname()))
                .body("imageUrl", is(updateRequest.getImageUrl()))
                .body("updated", notNullValue())
                .body("updated", not(equalTo(user.getCreated())));

        host().get("/users/{userId}", user.getId())
                .then().log().ifValidationFails()
                .statusCode(SUCCESS)
                .body("id", is(user.getId()))
                .body("email", is(updateRequest.getEmail()))
                .body("githubLogin", is(updateRequest.getGithubLogin()))
                .body("linkedinId", is(updateRequest.getLinkedinId()))
                .body("nickname", is(updateRequest.getNickname()))
                .body("imageUrl", is(updateRequest.getImageUrl()))
                .body("updated", notNullValue())
                .body("updated", not(equalTo(user.getCreated())));

        host().delete("/users/{userId}", user.getId())
                .then().log().ifValidationFails()
                .statusCode(SUCCESS)
                .body(isEmptyString());

        int usersCountAfterRemoval = host().get("/users").as(Collection.class).size();
        Assert.assertEquals(usersCountBefore, usersCountAfterRemoval);

        host().get("/users/{userId}", user.getId())
                .then().log().ifValidationFails()
                .statusCode(NOT_FOUND);
    }
}
