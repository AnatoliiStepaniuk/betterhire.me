package com.sdehunt.api;

import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

public abstract class AbstractApiTest {
    protected final static String BASE_URI = "http://localhost";
    protected final static int PORT = 8080;
    protected final static String APP_JSON = "application/json";
    protected final static int SUCCESS = 200;
    protected final static int NOT_FOUND = 404;

    protected RequestSpecification host() {
        RequestSpecification spec = given().baseUri(BASE_URI).log().ifValidationFails();
        if (PORT > 0) {
            spec = spec.port(PORT);
        }
        return spec;
    }
}
