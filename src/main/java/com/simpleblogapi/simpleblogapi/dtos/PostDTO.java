package com.simpleblogapi.simpleblogapi.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.simpleblogapi.simpleblogapi.enums.PostStatus;
import com.simpleblogapi.simpleblogapi.models.Category;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PostDTO {
    @JsonProperty("post_title")
    @Size(min = 4, max = 200, message = "Post title must be between 4 and 200 characters")
    private String postTitle;

    @JsonProperty("post_content")
    private String postContent;

    @JsonProperty("post_status")
    private PostStatus postStatus;

    @JsonProperty("category_id")
    private Long categoryId;
}

