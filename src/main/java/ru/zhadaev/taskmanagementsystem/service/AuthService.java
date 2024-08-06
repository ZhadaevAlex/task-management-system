package ru.zhadaev.taskmanagementsystem.service;

import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.zhadaev.taskmanagementsystem.controller.AuthController;
import ru.zhadaev.taskmanagementsystem.dto.JwtRequest;
import ru.zhadaev.taskmanagementsystem.exception.InvalidJwtAuthenticationException;
import ru.zhadaev.taskmanagementsystem.security.CustomUserDetailService;
import ru.zhadaev.taskmanagementsystem.security.JwtTokenUtils;

@Service
@AllArgsConstructor
@Transactional
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailService customUserDetailService;
    private final JwtTokenUtils jwtTokenUtils;

    public String createAuthToken(JwtRequest authRequest) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword()));
        } catch (BadCredentialsException e) {
            throw new InvalidJwtAuthenticationException(e.getMessage());
        }

        UserDetails userDetails = customUserDetailService.loadUserByUsername(authRequest.getEmail());
        return jwtTokenUtils.generateToken(userDetails);
    }
}
