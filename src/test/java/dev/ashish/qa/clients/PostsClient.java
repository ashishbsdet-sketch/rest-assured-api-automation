package dev.ashish.qa.clients;

import static dev.ashish.qa.specs.ApiSpecifications.defaultRequest;

import dev.ashish.qa.models.PostRequest;
import io.restassured.response.Response;
import java.util.Map;

public class PostsClient {
    public Response getPost(int postId) {
        return defaultRequest()
                .pathParam("postId", postId)
                .when()
                .get("/posts/{postId}");
    }

    public Response getPostsByUser(int userId) {
        return defaultRequest()
                .queryParam("userId", userId)
                .when()
                .get("/posts");
    }

    public Response createPost(PostRequest request) {
        return defaultRequest()
                .body(request)
                .when()
                .post("/posts");
    }

    public Response replacePost(int postId, PostRequest request) {
        return defaultRequest()
                .pathParam("postId", postId)
                .body(request)
                .when()
                .put("/posts/{postId}");
    }

    public Response updatePost(int postId, Map<String, ?> changes) {
        return defaultRequest()
                .pathParam("postId", postId)
                .body(changes)
                .when()
                .patch("/posts/{postId}");
    }

    public Response deletePost(int postId) {
        return defaultRequest()
                .pathParam("postId", postId)
                .when()
                .delete("/posts/{postId}");
    }
}
