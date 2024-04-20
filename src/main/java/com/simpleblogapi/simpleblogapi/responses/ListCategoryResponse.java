package com.simpleblogapi.simpleblogapi.responses;

import lombok.*;

import java.util.List;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ListCategoryResponse {
    private List<CategoryResponse> categories;
}
