package com.sdehunt.api;

import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

public abstract class AbstractApiTest {
    protected final static String BASE_URI = "http://localhost";
    protected final static int PORT = 80;
    protected final static String APP_JSON = "application/json";
    protected final static int SUCCESS = 200;

    protected RequestSpecification host() {
        return given().baseUri(BASE_URI).port(PORT);
    }
}
