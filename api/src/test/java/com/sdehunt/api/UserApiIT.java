package com.sdehunt.api;

import com.sdehunt.commons.model.User;
import com.sdehunt.commons.model.impl.UserImpl;
import com.sdehunt.dto.CreateUserDTO;
import io.restassured.response.Response;
import org.junit.Assert;
import org.junit.Test;

import java.util.Collection;
import java.util.UUID;

import static org.hamcrest.Matchers.isEmptyString;
import static org.hamcrest.core.Is.is;

public class UserApiIT extends AbstractApiTest {

    @Test
    public void crudTest() {
        String email = UUID.randomUUID().toString() + "@gmail.com";
        String github = "GH" + UUID.randomUUID().toString();
        String linkedIn = "LI" + UUID.randomUUID().toString();

        int usersCountBefore = host().get("/users").as(Collection.class).size();

        CreateUserDTO createRequest = new CreateUserDTO()
                .setEmail(email)
                .setGithub(github)
                .setLinkedIn(linkedIn);

        Response response = host()
                .contentType(APP_JSON)
                .body(createRequest)
                .post("/users");

        response.then().log().ifValidationFails()
                .statusCode(SUCCESS)
                .body("email", is(email))
                .body("github", is(github))
                .body("linkedIn", is(linkedIn));

        User user = response.as(UserImpl.class);

        host().get("/users/{userId}", user.getId())
                .then().log().ifValidationFails()
                .body("id", is(user.getId()))
                .body("email", is(user.getEmail()))
                .body("github", is(user.getGithub()))
                .body("linkedIn", is(user.getLinkedIn()));

        int usersCountAfter = host().get("/users").as(Collection.class).size();
        Assert.assertEquals(usersCountBefore + 1, usersCountAfter);

        user
                .setEmail(user.getEmail() + "2")
                .setGithub(user.getGithub() + "2")
                .setLinkedIn(user.getLinkedIn() + "2");

        host().contentType(APP_JSON)
                .body(user)
                .put("/users/{userId}", user.getId())
                .then().log().ifValidationFails()
                .statusCode(SUCCESS)
                .body("id", is(user.getId()))
                .body("email", is(user.getEmail()))
                .body("github", is(user.getGithub()))
                .body("linkedIn", is(user.getLinkedIn()));

        host().get("/users/{userId}", user.getId())
                .then().log().ifValidationFails()
                .statusCode(SUCCESS)
                .body("id", is(user.getId()))
                .body("email", is(user.getEmail()))
                .body("github", is(user.getGithub()))
                .body("linkedIn", is(user.getLinkedIn()));

        host().delete("/users/{userId}", user.getId())
                .then().log().ifValidationFails()
                .statusCode(SUCCESS)
                .body(isEmptyString());

        int usersCountAfterRemoval = host().get("/users").as(Collection.class).size();
        Assert.assertEquals(usersCountBefore, usersCountAfterRemoval);

        host().get("/users/{userId}", user.getId())
                .then().log().ifValidationFails()
                .statusCode(SUCCESS)
                .body(isEmptyString());
    }
}
