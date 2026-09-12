package dev.ashish.qa.models;

public record CommentResponse(
        Integer postId,
        Integer id,
        String name,
        String email,
        String body
) {
}
