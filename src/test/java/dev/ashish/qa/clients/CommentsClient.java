package dev.ashish.qa.clients;

import static dev.ashish.qa.specs.ApiSpecifications.defaultRequest;

import io.restassured.response.Response;

public class CommentsClient {
    public Response getCommentsForPost(int postId) {
        return defaultRequest()
                .pathParam("postId", postId)
                .when()
                .get("/posts/{postId}/comments");
    }

    public Response filterCommentsByPost(int postId) {
        return defaultRequest()
                .queryParam("postId", postId)
                .when()
                .get("/comments");
    }
}
