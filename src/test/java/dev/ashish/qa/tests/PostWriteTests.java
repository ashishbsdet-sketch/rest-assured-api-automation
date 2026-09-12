package dev.ashish.qa.tests;

import static dev.ashish.qa.specs.ApiSpecifications.defaultRequest;
import static dev.ashish.qa.specs.ApiSpecifications.successfulJsonResponse;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

import dev.ashish.qa.models.PostRequest;
import dev.ashish.qa.models.PostResponse;
import java.util.Map;
import org.testng.Assert;
import org.testng.annotations.Test;

public class PostWriteTests {
    @Test(groups = {"smoke", "regression"})
    public void createsPostFromTypedPayload() {
        PostRequest request = new PostRequest(
                7,
                "API quality review",
                "Validate the service contract before release"
        );

        PostResponse created = defaultRequest()
                .body(request)
                .when()
                .post("/posts")
                .then()
                .spec(successfulJsonResponse(201))
                .body("id", notNullValue())
                .extract()
                .as(PostResponse.class);

        Assert.assertEquals(created.userId(), request.userId());
        Assert.assertEquals(created.title(), request.title());
        Assert.assertEquals(created.body(), request.body());
    }

    @Test(groups = "regression")
    public void replacesExistingPost() {
        PostRequest replacement = new PostRequest(
                2,
                "Updated contract",
                "The full resource has been replaced"
        );

        defaultRequest()
                .pathParam("postId", 10)
                .body(replacement)
                .when()
                .put("/posts/{postId}")
                .then()
                .spec(successfulJsonResponse(200))
                .body("id", equalTo(10))
                .body("userId", equalTo(2))
                .body("title", equalTo(replacement.title()))
                .body("body", equalTo(replacement.body()));
    }

    @Test(groups = "regression")
    public void updatesOneFieldWithoutReplacingTheResource() {
        defaultRequest()
                .pathParam("postId", 12)
                .body(Map.of("title", "Reviewed by QA"))
                .when()
                .patch("/posts/{postId}")
                .then()
                .spec(successfulJsonResponse(200))
                .body("id", equalTo(12))
                .body("title", equalTo("Reviewed by QA"))
                .body("userId", notNullValue());
    }

    @Test(groups = "regression")
    public void deletesExistingPost() {
        defaultRequest()
                .pathParam("postId", 15)
                .when()
                .delete("/posts/{postId}")
                .then()
                .spec(successfulJsonResponse(200))
                .body("$", equalTo(Map.of()));
    }
}
