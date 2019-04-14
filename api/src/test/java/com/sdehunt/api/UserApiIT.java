package com.sdehunt.api;

import com.sdehunt.commons.model.User;
import com.sdehunt.commons.model.impl.UserImpl;
import io.restassured.response.Response;
import org.junit.Test;

import java.util.UUID;

import static org.hamcrest.Matchers.isEmptyString;
import static org.hamcrest.core.Is.is;

public class UserApiIT extends AbstractApiTest {

    @Test
    public void crudTest() {
        String email = UUID.randomUUID().toString() + "@gmail.com";
        String github = "GH" + UUID.randomUUID().toString();
        String linkedIn = "LI" + UUID.randomUUID().toString();

        Response response = host()
                .contentType(APP_JSON)
                .body(new UserImpl().setEmail(email))
                .post("/users");

        response.then().log().ifValidationFails()
                .statusCode(SUCCESS)
                .body("email", is(email));

        User user = response.as(UserImpl.class);

        host().get("/users/{userId}", user.getId())
                .then().log().ifValidationFails()
                .body("id", is(user.getId()))
                .body("email", is(user.getEmail()));

        host().contentType(APP_JSON)
                .body(user.setGithub(github).setLinkedIn(linkedIn))
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

        host().get("/users/{userId}", user.getId())
                .then().log().ifValidationFails()
                .statusCode(SUCCESS)
                .body(isEmptyString());
    }
}
