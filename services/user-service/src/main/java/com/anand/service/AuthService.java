package com.anand.service;

import com.anand.payload.dto.UserDTO;
import com.anand.payload.response.AuthResponse;

public interface AuthService {

    AuthResponse login(String email, String password) throws Exception;
    AuthResponse signup(UserDTO req) throws Exception;
}
