package com.anand.service.Impl;

import com.anand.config.JwtProvider;
import com.anand.enums.UserRole;
import com.anand.mapper.UserMapper;
import com.anand.model.User;
import com.anand.payload.dto.UserDTO;
import com.anand.payload.response.AuthResponse;
import com.anand.repository.UserRepository;
import com.anand.service.AuthService;
import com.anand.service.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final CustomUserDetailsService customUserDetailsService;


    @Override
    public AuthResponse signup(UserDTO req)throws Exception {
        User existingUser = userRepository.findByEmail(req.getEmail());
        if(existingUser != null){
            throw new Exception("Email already Registered!");
        }
        if(req.getRole() == UserRole.ROLE_SYSTEM_ADMIN){
            throw new Exception("You cannot signup System Admin!");
        }

        User newUser = User.builder()
                .email(req.getEmail())
                .password(passwordEncoder.encode(req.getPassword()))
                .phone(req.getPhone())
                .role(req.getRole())
                .fullName(req.getFullName())
                .lastLogin(LocalDateTime.now())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        User savedUser = userRepository.save(newUser);

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                savedUser.getEmail(),savedUser.getPassword()
        );

        String jwt = jwtProvider.generateToken(
                authentication, savedUser.getId()
        );

        AuthResponse authResponse = new AuthResponse();
        authResponse.setJwt(jwt);
        authResponse.setUser(UserMapper.toDTO(savedUser));
        authResponse.setTitle("Welcome " + savedUser.getFullName());
        authResponse.setMessage("Registered Successfully!");
        return authResponse;
    }

    @Override
    public AuthResponse login(String email, String password) throws Exception {
        Authentication authentication = authenticate(email, password);

        User user = userRepository.findByEmail(email);
        user.setLastLogin(LocalDateTime.now());
        userRepository.save(user);

        String jwt = jwtProvider.generateToken(authentication,user.getId());
        AuthResponse authResponse = new AuthResponse();
        authResponse.setJwt(jwt);
        authResponse.setUser(UserMapper.toDTO(user));
        authResponse.setTitle("Welcome back "+user.getFullName());
        authResponse.setMessage("Logged in Successfully!");
        return authResponse;
    }


    private Authentication authenticate(String email, String password) throws Exception {
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(email);

        if(!passwordEncoder.matches(
                password,userDetails.getPassword()
        )){
            throw new Exception("invalid password");
        }
        return new UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                userDetails.getAuthorities()
        );
    }


}
