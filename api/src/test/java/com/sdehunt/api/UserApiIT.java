package com.sdehunt.api;

import com.sdehunt.commons.model.Language;
import com.sdehunt.commons.model.User;
import com.sdehunt.dto.CreateUserDTO;
import com.sdehunt.repository.UserQuery;
import io.restassured.response.Response;
import org.junit.Assert;
import org.junit.Test;

import java.util.*;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.Is.is;

public class UserApiIT extends AbstractApiTest {

    @Test
    public void crudTest() {
        String email = UUID.randomUUID().toString() + "@gmail.com";
        String githubLogin = "GH_" + UUID.randomUUID().toString();
        String linkedInId = "LI_" + UUID.randomUUID().toString();
        String nickname = "NN_" + UUID.randomUUID().toString();
        String imageUrl = "IMG_" + UUID.randomUUID().toString();
        String name = "NAME_" + UUID.randomUUID().toString();
        String cv = "CV_" + UUID.randomUUID().toString();
        String phone = "PHONE_" + UUID.randomUUID().toString().substring(0, 10);
        String city = "CITY_" + UUID.randomUUID().toString();
        String company = "COMPANY_" + UUID.randomUUID().toString();
        Language language = Language.values()[new Random().nextInt(Language.values().length)];
        Set<Language> languages = Collections.singleton(language);

        int usersCountBefore = host().get("/users?test=true").as(Collection.class).size();

        CreateUserDTO createRequest = new CreateUserDTO()
                .setEmail(email)
                .setGithubLogin(githubLogin)
                .setLinkedinId(linkedInId)
                .setCompany(company)
                .setNickname(nickname)
                .setImageUrl(imageUrl)
                .setName(name)
                .setPhone(phone)
                .setCv(cv)
                .setCity(city)
                .setLanguages(languages)
                .setTest(true);

        Response response = host()
                .contentType(APP_JSON)
                .body(createRequest)
                .post("/users");

        response.then().log().ifValidationFails()
                .statusCode(SUCCESS)
                .body("email", is(email))
                .body("githubLogin", is(githubLogin))
                .body("linkedinId", is(linkedInId))
                .body("company", is(company))
                .body("nickname", is(nickname))
                .body("imageUrl", is(imageUrl))
                .body("userName", is(nickname))
                .body("cv", is(cv))
                .body("city", is(city))
                .body("languages", contains(language.name()))
                .body("phone", is(phone))
                .body("solved", is(0))
                .body("avgRank", equalTo(null))
                .body("test", is(true))
                .body("available", is(true))
                .body("manager", is(false));

        User user = response.as(User.class);

        // Get by id test
        host().get("/users/{userId}", user.getId())
                .then().log().ifValidationFails()
                .body("id", is(user.getId()))
                .body("email", is(user.getEmail()))
                .body("githubLogin", is(user.getGithubLogin()))
                .body("linkedinId", is(user.getLinkedinId()))
                .body("company", is(user.getCompany()))
                .body("nickname", is(user.getNickname()))
                .body("imageUrl", is(user.getImageUrl()))
                .body("userName", is(nickname))
                .body("cv", is(cv))
                .body("city", is(city))
                .body("languages", contains(language.name()))
                .body("phone", is(phone))
                .body("solved", is(0))
                .body("avgRank", equalTo(null))
                .body("created", notNullValue())
                .body("updated", notNullValue())
                .body("test", is(true))
                .body("available", is(true))
                .body("manager", is(false));

        // Query test
        UserQuery query = new UserQuery()
                .setGithubLogin(githubLogin)
                .setEmail(email)
                .setLinkedinId(linkedInId)
                .setNickname(nickname)
                .setTest(true);
        host().contentType(APP_JSON)
                .body(query)
                .post("/users/query")
                .then()
                .body("[0].id", is(user.getId()))
                .body("[0].email", is(user.getEmail()))
                .body("[0].githubLogin", is(user.getGithubLogin()))
                .body("[0].linkedinId", is(user.getLinkedinId()))
                .body("[0].company", is(user.getCompany()))
                .body("[0].nickname", is(user.getNickname()))
                .body("[0].cv", is(user.getCv()))
                .body("[0].languages", contains(language.name()))
                .body("[0].city", is(user.getCity()))
                .body("[0].phone", is(user.getPhone()))
                .body("[0].imageUrl", is(user.getImageUrl()))
                .body("[0].userName", is(nickname))
                .body("[0].solved", is(0))
                .body("[0].avgRank", equalTo(null))
                .body("[0].created", notNullValue())
                .body("[0].updated", notNullValue())
                .body("[0].test", is(true))
                .body("[0].available", is(true))
                .body("[0].manager", is(false));

        int usersCountAfter = host().get("/users?test=true").as(Collection.class).size();
        Assert.assertEquals(usersCountBefore + 1, usersCountAfter);

        Language updatedLanguage = Language.values()[new Random().nextInt(Language.values().length)];
        Set<Language> newLanguages = Collections.singleton(updatedLanguage);
        CreateUserDTO updateRequest = new CreateUserDTO()
                .setEmail(user.getEmail() + "2")
                .setGithubLogin(user.getGithubLogin() + "2")
                .setLinkedinId(user.getLinkedinId() + "2")
                .setCompany(user.getCompany() + "2")
                .setNickname(user.getNickname() + "2")
                .setImageUrl(user.getImageUrl() + "2")
                .setPhone(user.getPhone() + "2")
                .setCv(user.getCv() + "2")
                .setCity(user.getCity() + "2")
                .setLanguages(newLanguages)
                .setAvailable(false)
                .setManager(true);

        host().contentType(APP_JSON)
                .body(updateRequest)
                .put("/users/{userId}", user.getId())
                .then().log().ifValidationFails()
                .statusCode(SUCCESS)
                .body("id", is(user.getId()))
                .body("email", is(updateRequest.getEmail()))
                .body("githubLogin", is(updateRequest.getGithubLogin()))
                .body("linkedinId", is(updateRequest.getLinkedinId()))
                .body("company", is(updateRequest.getCompany()))
                .body("nickname", is(updateRequest.getNickname()))
                .body("imageUrl", is(updateRequest.getImageUrl()))
                .body("cv", is(updateRequest.getCv()))
                .body("city", is(updateRequest.getCity()))
                .body("languages", contains(updatedLanguage.name()))
                .body("phone", is(updateRequest.getPhone()))
                .body("userName", is(updateRequest.getNickname()))
                .body("solved", is(0))
                .body("avgRank", equalTo(null))
                .body("updated", notNullValue())
                .body("updated", not(equalTo(user.getCreated())))
                .body("test", is(true))
                .body("available", is(false))
                .body("manager", is(true));

        host().get("/users/{userId}", user.getId())
                .then().log().ifValidationFails()
                .statusCode(SUCCESS)
                .body("id", is(user.getId()))
                .body("email", is(updateRequest.getEmail()))
                .body("githubLogin", is(updateRequest.getGithubLogin()))
                .body("linkedinId", is(updateRequest.getLinkedinId()))
                .body("company", is(updateRequest.getCompany()))
                .body("nickname", is(updateRequest.getNickname()))
                .body("imageUrl", is(updateRequest.getImageUrl()))
                .body("userName", is(updateRequest.getNickname()))
                .body("cv", is(updateRequest.getCv()))
                .body("city", is(updateRequest.getCity()))
                .body("languages", contains(updatedLanguage.name()))
                .body("phone", is(updateRequest.getPhone()))
                .body("solved", is(0))
                .body("avgRank", equalTo(null))
                .body("updated", notNullValue())
                .body("updated", not(equalTo(user.getCreated())))
                .body("test", is(true))
                .body("available", is(false))
                .body("manager", is(true));

        host().delete("/users/{userId}", user.getId())
                .then().log().ifValidationFails()
                .statusCode(SUCCESS)
                .body(isEmptyString());

        int usersCountAfterRemoval = host().get("/users?test=true").as(Collection.class).size();
        Assert.assertEquals(usersCountBefore, usersCountAfterRemoval);

        host().get("/users/{userId}", user.getId())
                .then().log().ifValidationFails()
                .statusCode(NOT_FOUND);
    }
}
