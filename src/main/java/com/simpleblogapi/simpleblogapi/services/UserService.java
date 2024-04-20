package com.simpleblogapi.simpleblogapi.services;

import com.simpleblogapi.simpleblogapi.dtos.UserDTO;
import com.simpleblogapi.simpleblogapi.exceptions.DataNotFoundException;
import com.simpleblogapi.simpleblogapi.models.Role;
import com.simpleblogapi.simpleblogapi.models.User;
import com.simpleblogapi.simpleblogapi.repositories.RoleRepository;
import com.simpleblogapi.simpleblogapi.repositories.UserRepository;
import com.simpleblogapi.simpleblogapi.responses.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class UserService implements IUserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    @Override
    public Page<UserResponse> findAll(PageRequest pageRequest) {
      return userRepository.findAll(pageRequest).map(UserResponse::fromUser);
    }

    @Override
    public void deleteUser(Long id) throws DataNotFoundException {
       User user = userRepository.findById(id).orElseThrow(
                () -> new DataNotFoundException("User not found with id: " + id)
        );
        userRepository.delete(user);
    }

    @Override
    public UserResponse updateUser(Long id, UserDTO userDTO) throws DataNotFoundException {
        User user = userRepository.findById(id).orElseThrow(
                () -> new DataNotFoundException("User not found with id: " + id)
        );
        Role role = roleRepository.findById(userDTO.getRoleId()).orElseThrow(
                () -> new DataNotFoundException("Role not found with id: " + userDTO.getRoleId())
        );
        if(userRepository.existsByEmail(userDTO.getEmail()) && !user.getEmail().equals(userDTO.getEmail())) {
            throw new DataNotFoundException("Email already exists");
        }

        user.setFullName(userDTO.getFullName());
        user.setEmail(userDTO.getEmail());
        user.setRole(role);
        return UserResponse.fromUser(userRepository.save(user));
    }
}
