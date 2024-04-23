package com.simpleblogapi.simpleblogapi.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreatePostDTO {
    @JsonProperty("post_title")
    @NotBlank(message = "Post title is required")
    @Size(min = 4, max = 200, message = "Post title must be between 4 and 200 characters")
    private String postTitle;

    @JsonProperty("post_content")
    private String postContent;

    @JsonProperty("category_id")
    private Long categoryId;

    @JsonProperty("user_id")
    private Long userId;
}
