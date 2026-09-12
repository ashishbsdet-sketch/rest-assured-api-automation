package dev.ashish.qa.tests;

import static dev.ashish.qa.specs.ApiSpecifications.defaultRequest;
import static dev.ashish.qa.specs.ApiSpecifications.successfulJsonResponse;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.everyItem;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

import dev.ashish.qa.data.PostData;
import dev.ashish.qa.models.PostResponse;
import java.util.Map;
import org.testng.Assert;
import org.testng.annotations.Test;

public class PostReadTests {
    @Test(groups = {"smoke", "contract"})
    public void returnsPostByIdWithExpectedContract() {
        PostResponse post = defaultRequest()
                .pathParam("postId", 1)
                .when()
                .get("/posts/{postId}")
                .then()
                .spec(successfulJsonResponse(200))
                .body(matchesJsonSchemaInClasspath("schemas/post-schema.json"))
                .extract()
                .as(PostResponse.class);

        Assert.assertEquals(post.id(), 1);
        Assert.assertEquals(post.userId(), 1);
        Assert.assertFalse(post.title().isBlank());
        Assert.assertFalse(post.body().isBlank());
    }

    @Test(dataProvider = "existingPostIds", dataProviderClass = PostData.class,
            groups = "regression")
    public void returnsExistingPosts(int postId, int expectedUserId) {
        defaultRequest()
                .pathParam("postId", postId)
                .when()
                .get("/posts/{postId}")
                .then()
                .spec(successfulJsonResponse(200))
                .body("id", equalTo(postId))
                .body("userId", equalTo(expectedUserId))
                .body("title", is(not(empty())));
    }

    @Test(groups = "regression")
    public void filtersPostsByUserId() {
        defaultRequest()
                .queryParam("userId", 4)
                .when()
                .get("/posts")
                .then()
                .spec(successfulJsonResponse(200))
                .body("size()", greaterThan(0))
                .body("userId", everyItem(equalTo(4)));
    }

    @Test(groups = {"negative", "regression"})
    public void returnsEmptyObjectForUnknownPost() {
        Map<?, ?> response = defaultRequest()
                .pathParam("postId", 999_999)
                .when()
                .get("/posts/{postId}")
                .then()
                .statusCode(404)
                .header("Content-Type", org.hamcrest.Matchers.containsString("application/json"))
                .extract()
                .as(Map.class);

        Assert.assertTrue(response.isEmpty(), "Unknown posts should not return resource data");
    }
}
