package com.simpleblogapi.simpleblogapi.services;

import com.simpleblogapi.simpleblogapi.dtos.UserDTO;
import com.simpleblogapi.simpleblogapi.exceptions.DataNotFoundException;
import com.simpleblogapi.simpleblogapi.responses.UserResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public interface IUserService {
    // Get all users
    Page<UserResponse> findAll(PageRequest pageRequest);
    // Delete user by id
    void deleteUser(Long id) throws DataNotFoundException;
    // Update user by id
    UserResponse updateUser(Long id, UserDTO userDTO) throws DataNotFoundException;
}
