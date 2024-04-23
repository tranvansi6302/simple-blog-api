package com.simpleblogapi.simpleblogapi.services;

import com.simpleblogapi.simpleblogapi.components.JwtTokenUtil;
import com.simpleblogapi.simpleblogapi.dtos.ProfileDTO;
import com.simpleblogapi.simpleblogapi.dtos.UpdateUserDTO;
import com.simpleblogapi.simpleblogapi.exceptions.DataNotFoundException;
import com.simpleblogapi.simpleblogapi.models.Role;
import com.simpleblogapi.simpleblogapi.models.User;
import com.simpleblogapi.simpleblogapi.repositories.RoleRepository;
import com.simpleblogapi.simpleblogapi.repositories.UserRepository;
import com.simpleblogapi.simpleblogapi.responses.UserResponse;
import com.simpleblogapi.simpleblogapi.utils.CheckCondition;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
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
    public UserResponse updateUser(Long id, UpdateUserDTO updateUserDTO) throws DataNotFoundException {
        User user = userRepository.findById(id).orElseThrow(
                () -> new DataNotFoundException("User not found with id: " + id)
        );
        Role role = roleRepository.findById(updateUserDTO.getRoleId()).orElseThrow(
                () -> new DataNotFoundException("Role not found with id: " + updateUserDTO.getRoleId())
        );
        if (userRepository.existsByEmail(updateUserDTO.getEmail()) && !user.getEmail().equals(updateUserDTO.getEmail())) {
            throw new DataNotFoundException("Email already exists");
        }

        BeanUtils.copyProperties(updateUserDTO, user, CheckCondition.getNullPropertyNames(updateUserDTO));
        user.setRole(role);
        return UserResponse.fromUser(userRepository.save(user));
    }

    @Override
    public UserResponse getMe() throws DataNotFoundException {
        Long userId = JwtTokenUtil.getUserIdFormToken();
        User user = userRepository.findById(userId).orElseThrow(
                () -> new DataNotFoundException("User not found with id: " + userId)
        );
        return UserResponse.fromUser(user);

    }

    @Override
    public UserResponse updateMe(ProfileDTO profileDTO) throws DataNotFoundException {
        Long userId = JwtTokenUtil.getUserIdFormToken();
        User user = userRepository.findById(userId).orElseThrow(
                () -> new DataNotFoundException("User not found with id: " + userId)
        );
        if (userRepository.existsByEmail(profileDTO.getEmail()) && !user.getEmail().equals(profileDTO.getEmail())) {
            throw new DataNotFoundException("Email already exists");
        }
        BeanUtils.copyProperties(profileDTO, user, CheckCondition.getNullPropertyNames(profileDTO));
        return UserResponse.fromUser(userRepository.save(user));
    }

    @Override
    public UserResponse uploadAvatar(String imageUrl) throws DataNotFoundException {
        Long userId = JwtTokenUtil.getUserIdFormToken();
        User user = userRepository.findById(userId).orElseThrow(
                () -> new DataNotFoundException("User not found with id: " + userId)
        );
        user.setAvatar(imageUrl);
        return UserResponse.fromUser(userRepository.save(user));
    }

}
