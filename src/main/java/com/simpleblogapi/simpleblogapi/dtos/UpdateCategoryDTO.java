package com.simpleblogapi.simpleblogapi.dtos;


import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateCategoryDTO {

    @JsonProperty("category_name")
    @Size(min = 4, max = 100, message = "Category name id must be between 4 and 100 characters")
    private String categoryName;
}
