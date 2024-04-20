package com.simpleblogapi.simpleblogapi.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.simpleblogapi.simpleblogapi.models.Role;
import com.simpleblogapi.simpleblogapi.models.User;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponse {
    @JsonProperty("user_id")
    private Long userId;

    @JsonProperty("fullname")
    private String fullName;

    @JsonProperty("email")
    private String email;

    @JsonProperty("password")
    private String password;

    @JsonProperty("avatar")
    private String avatar;

    @JsonProperty("role_name")
    private String roleName;

    public static UserResponse fromUser(User user) {
        return UserResponse.builder()
                .userId(user.getUserId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .password(user.getPassword())
                .avatar(user.getAvatar())
                .roleName(user.getRole().getRoleName())
                .build();
    }

}
