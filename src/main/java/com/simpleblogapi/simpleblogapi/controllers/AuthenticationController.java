package com.simpleblogapi.simpleblogapi.controllers;

import com.simpleblogapi.simpleblogapi.dtos.LoginDTO;
import com.simpleblogapi.simpleblogapi.dtos.RegisterDTO;
import com.simpleblogapi.simpleblogapi.responses.AuthenticationResponse;
import com.simpleblogapi.simpleblogapi.responses.LoginResponse;
import com.simpleblogapi.simpleblogapi.services.IAuthenticationService;
import com.simpleblogapi.simpleblogapi.services.IUserService;
import com.simpleblogapi.simpleblogapi.utils.Validator;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.naming.Binding;
import java.util.Collections;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class AuthenticationController {
    private final IAuthenticationService authenticationService;
    @PostMapping("/register")
    public ResponseEntity<?> register(
            @Valid @RequestBody RegisterDTO registerDTO,
            BindingResult result
    ) {
        if(result.hasErrors()) {
            return ResponseEntity.badRequest().body(Validator.getMessageValidator(result));
        }

        try {
            AuthenticationResponse user = authenticationService.register(registerDTO);
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("message", e.getMessage()));
        }

    }

    @PostMapping("/login")
    public ResponseEntity<?> login(
            @Valid @RequestBody LoginDTO loginDTO,
            BindingResult result
    ) {
        try {
            if(result.hasErrors()) {
                return ResponseEntity.badRequest().body(Validator.getMessageValidator(result));
            }
            LoginResponse loginResponse = authenticationService.login(loginDTO);
            return ResponseEntity.ok(loginResponse);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("message", e.getMessage()));
        }

    }
}
