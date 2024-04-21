package com.simpleblogapi.simpleblogapi.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.simpleblogapi.simpleblogapi.models.User;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthenticationResponse {
    @JsonProperty("user_id")
    private Long userId;
    @JsonProperty("fullname")
    private String fullName;
    private String email;

    public static AuthenticationResponse fromUser(User user) {
        return AuthenticationResponse.builder()
                .userId(user.getUserId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .build();
    }
}
