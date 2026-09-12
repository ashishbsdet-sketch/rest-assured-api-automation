package dev.ashish.qa.specs;

import static io.restassured.RestAssured.with;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.lessThan;

import dev.ashish.qa.config.ApiConfig;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

public final class ApiSpecifications {
    private ApiSpecifications() {
    }

    public static RequestSpecification defaultRequest() {
        return with()
                .baseUri(ApiConfig.baseUrl())
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .log().ifValidationFails();
    }

    public static ResponseSpecification successfulJsonResponse(int statusCode) {
        return new ResponseSpecBuilder()
                .expectStatusCode(statusCode)
                .expectHeader("Content-Type", containsString("application/json"))
                .expectResponseTime(lessThan(10_000L))
                .build();
    }
}
