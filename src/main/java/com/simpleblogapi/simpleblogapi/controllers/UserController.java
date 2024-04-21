package com.simpleblogapi.simpleblogapi.controllers;

import com.simpleblogapi.simpleblogapi.dtos.UserDTO;
import com.simpleblogapi.simpleblogapi.models.User;
import com.simpleblogapi.simpleblogapi.responses.ListUserResponse;
import com.simpleblogapi.simpleblogapi.responses.PaginationResponse;
import com.simpleblogapi.simpleblogapi.responses.UserResponse;
import com.simpleblogapi.simpleblogapi.services.IUserService;
import com.simpleblogapi.simpleblogapi.utils.Validator;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final IUserService userService;
    @GetMapping("")
    public ResponseEntity<?> getAllUsers(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "limit", defaultValue = "10") int limit
    ) {
        PageRequest pageRequest = PageRequest.of(page - 1, limit, Sort.by("createdAt").descending());
        Page<UserResponse> userPage = userService.findAll(pageRequest);
        List<UserResponse> users = userPage.getContent();
        PaginationResponse pagination = new PaginationResponse(page, limit, userPage.getTotalPages());
        ListUserResponse listUserResponse = new ListUserResponse(users,pagination);
        return ResponseEntity.ok(listUserResponse);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        try {
            userService.deleteUser(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("message", e.getMessage()));
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UserDTO userDTO,
            BindingResult result
    ) {
        try {
            if(result.hasErrors()) {
                return ResponseEntity.badRequest().body(Validator.getMessageValidator(result));
            }
            UserResponse user = userService.updateUser(id, userDTO);
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("message", e.getMessage()));
        }
    }

    @GetMapping("/me")
    public ResponseEntity<?> getMe() {
        try {
            User user = userService.getMe();
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("message", e.getMessage()));
        }
    }
}
