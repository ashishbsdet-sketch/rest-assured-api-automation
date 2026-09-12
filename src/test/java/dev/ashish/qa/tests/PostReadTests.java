package dev.ashish.qa.tests;

import static dev.ashish.qa.specs.ApiSpecifications.successfulJsonResponse;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.everyItem;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

import dev.ashish.qa.clients.PostsClient;
import dev.ashish.qa.data.PostData;
import dev.ashish.qa.models.PostResponse;
import java.util.Map;
import org.testng.Assert;
import org.testng.annotations.Test;

public class PostReadTests {
    private final PostsClient posts = new PostsClient();

    @Test(groups = {"smoke", "contract"})
    public void returnsPostByIdWithExpectedContract() {
        PostResponse post = posts.getPost(1)
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
        posts.getPost(postId)
                .then()
                .spec(successfulJsonResponse(200))
                .body("id", equalTo(postId))
                .body("userId", equalTo(expectedUserId))
                .body("title", is(not(empty())));
    }

    @Test(groups = {"regression", "contract"})
    public void filtersPostsByUserId() {
        posts.getPostsByUser(4)
                .then()
                .spec(successfulJsonResponse(200))
                .body(matchesJsonSchemaInClasspath("schemas/posts-schema.json"))
                .body("size()", greaterThan(0))
                .body("userId", everyItem(equalTo(4)));
    }

    @Test(groups = {"negative", "regression"})
    public void returnsEmptyObjectForUnknownPost() {
        Map<?, ?> response = posts.getPost(999_999)
                .then()
                .statusCode(404)
                .header("Content-Type", containsString("application/json"))
                .extract()
                .as(Map.class);

        Assert.assertTrue(response.isEmpty(), "Unknown posts should not return resource data");
    }
}
