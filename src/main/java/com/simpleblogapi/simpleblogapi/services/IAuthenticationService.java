package com.simpleblogapi.simpleblogapi.services;

import com.simpleblogapi.simpleblogapi.dtos.LoginDTO;
import com.simpleblogapi.simpleblogapi.dtos.RegisterDTO;
import com.simpleblogapi.simpleblogapi.exceptions.DataNotFoundException;
import com.simpleblogapi.simpleblogapi.exceptions.ExistedDataException;
import com.simpleblogapi.simpleblogapi.responses.AuthenticationResponse;
import com.simpleblogapi.simpleblogapi.responses.LoginResponse;

public interface IAuthenticationService {
    LoginResponse login(LoginDTO loginDTO) throws DataNotFoundException;
    AuthenticationResponse register(RegisterDTO registerDTO) throws ExistedDataException;
}
