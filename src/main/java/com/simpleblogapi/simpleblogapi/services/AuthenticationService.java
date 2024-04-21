package com.simpleblogapi.simpleblogapi.services;

import com.simpleblogapi.simpleblogapi.components.JwtTokenUtil;
import com.simpleblogapi.simpleblogapi.dtos.LoginDTO;
import com.simpleblogapi.simpleblogapi.dtos.RegisterDTO;
import com.simpleblogapi.simpleblogapi.exceptions.DataNotFoundException;
import com.simpleblogapi.simpleblogapi.exceptions.ExistedDataException;
import com.simpleblogapi.simpleblogapi.models.Role;
import com.simpleblogapi.simpleblogapi.models.User;
import com.simpleblogapi.simpleblogapi.repositories.RoleRepository;
import com.simpleblogapi.simpleblogapi.repositories.UserRepository;
import com.simpleblogapi.simpleblogapi.responses.AuthenticationResponse;
import com.simpleblogapi.simpleblogapi.responses.LoginResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationService implements IAuthenticationService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtil jwtTokenUtil;
    private final AuthenticationManager authenticationManager;


    @Override
    public LoginResponse login(LoginDTO loginDTO) throws DataNotFoundException {
        User user = userRepository.findByEmail(loginDTO.getEmail())
                .orElseThrow(() -> new DataNotFoundException("Email or password is incorrect"));
        if (!passwordEncoder.matches(loginDTO.getPassword(), user.getPassword())) {
            throw new DataNotFoundException("Email or password is incorrect");
        }
        // Verify user with email and password
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(
                        loginDTO.getEmail(),
                        loginDTO.getPassword()
                );
        authenticationManager.authenticate(
                usernamePasswordAuthenticationToken
        );
        // Verify success -> generate token
        String token = jwtTokenUtil.generateToken(user);
        AuthenticationResponse userResponse = AuthenticationResponse.fromUser(user);
        return new LoginResponse(token, userResponse);

    }

    @Override
    public AuthenticationResponse register(RegisterDTO registerDTO) throws ExistedDataException {
        if (userRepository.existsByEmail(registerDTO.getEmail())) {
            throw new ExistedDataException("Email already exists");
        }
        if (!roleRepository.existsByRoleName("User")) {
            roleRepository.save(Role.builder()
                    .roleId(2L)
                    .roleName("User")
                    .build());
        }

        User user = User.builder()
                .fullName(registerDTO.getFullName())
                .email(registerDTO.getEmail())
                .password(passwordEncoder.encode(registerDTO.getPassword()))
                .avatar("avatar.png")
                .role(roleRepository.findByRoleName("User"))
                .build();
        return AuthenticationResponse.fromUser(userRepository.save(user));
    }
}
