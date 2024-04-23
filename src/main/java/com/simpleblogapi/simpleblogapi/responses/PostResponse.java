package com.simpleblogapi.simpleblogapi.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.simpleblogapi.simpleblogapi.enums.PostStatus;
import com.simpleblogapi.simpleblogapi.models.Category;
import com.simpleblogapi.simpleblogapi.models.Post;
import com.simpleblogapi.simpleblogapi.models.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostResponse {
    @JsonProperty("post_id")
    private Long postId;

    @JsonProperty("post_title")
    private String postTitle;

    @JsonProperty("post_thumbnail")
    private String postThumbnail;

    @JsonProperty("post_content")
    private String postContent;

    @JsonProperty("post_status")
    private PostStatus postStatus;

    @JsonProperty("user")
    private UserResponse user;

    @JsonProperty("category")
    private CategoryResponse category;

    public static PostResponse fromPost(Post post) {

        return PostResponse.builder()
                .postId(post.getPostId())
                .postTitle(post.getPostTitle())
                .postThumbnail(post.getPostThumbnail())
                .postContent(post.getPostContent())
                .postStatus(post.getPostStatus())
                .user(UserResponse.fromUser(post.getUser()))
                .category(CategoryResponse.fromCategory(post.getCategory()))
                .build();
    }
}
