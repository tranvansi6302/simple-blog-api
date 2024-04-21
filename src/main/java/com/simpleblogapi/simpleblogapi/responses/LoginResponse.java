package com.simpleblogapi.simpleblogapi.responses;

import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginResponse {
    private String token;
    private AuthenticationResponse user;
}
