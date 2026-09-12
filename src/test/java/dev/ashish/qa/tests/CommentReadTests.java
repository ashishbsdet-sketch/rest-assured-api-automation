package dev.ashish.qa.tests;

import static dev.ashish.qa.specs.ApiSpecifications.successfulJsonResponse;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.everyItem;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.matchesPattern;

import dev.ashish.qa.clients.CommentsClient;
import dev.ashish.qa.models.CommentResponse;
import java.util.List;
import org.testng.Assert;
import org.testng.annotations.Test;

public class CommentReadTests {
    private final CommentsClient comments = new CommentsClient();

    @Test(groups = {"smoke", "contract"})
    public void returnsCommentsWithValidContractAndEmailFormat() {
        List<CommentResponse> response = comments.getCommentsForPost(1)
                .then()
                .spec(successfulJsonResponse(200))
                .body(matchesJsonSchemaInClasspath("schemas/comments-schema.json"))
                .body("size()", greaterThan(0))
                .body("postId", everyItem(equalTo(1)))
                .body("email", everyItem(matchesPattern("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")))
                .extract()
                .jsonPath()
                .getList(".", CommentResponse.class);

        Assert.assertFalse(response.isEmpty());
        response.forEach(comment -> Assert.assertEquals(comment.postId(), 1));
    }

    @Test(groups = "regression")
    public void nestedAndFilteredCommentEndpointsReturnSameRecords() {
        List<CommentResponse> nested = comments.getCommentsForPost(3)
                .then()
                .spec(successfulJsonResponse(200))
                .extract()
                .jsonPath()
                .getList(".", CommentResponse.class);

        List<CommentResponse> filtered = comments.filterCommentsByPost(3)
                .then()
                .spec(successfulJsonResponse(200))
                .extract()
                .jsonPath()
                .getList(".", CommentResponse.class);

        Assert.assertEquals(filtered, nested, "Both supported routes should return the same comments");
    }

    @Test(groups = {"negative", "regression"})
    public void returnsEmptyCollectionForUnknownPostComments() {
        comments.getCommentsForPost(999_999)
                .then()
                .spec(successfulJsonResponse(200))
                .body("$", empty());
    }
}
