package com.simpleblogapi.simpleblogapi.responses;

import lombok.*;

import java.util.List;
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ListUserResponse {
    private List<UserResponse> users;
    private PaginationResponse pagination;
}
