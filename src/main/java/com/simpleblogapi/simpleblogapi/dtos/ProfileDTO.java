package com.simpleblogapi.simpleblogapi.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProfileDTO {
    @JsonProperty("fullname")
    @Size(min = 4, max = 50, message = "Fullname must be between 4 and 50 characters")
    private String fullName;

    @Email(message = "Email is not valid")
    private String email;
}
