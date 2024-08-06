package ru.zhadaev.taskmanagementsystem.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.zhadaev.taskmanagementsystem.dto.JwtRequest;
import ru.zhadaev.taskmanagementsystem.dto.JwtResponse;
import ru.zhadaev.taskmanagementsystem.service.AuthService;

@RestController
@AllArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;

    @PostMapping
    public ResponseEntity<?> createAuthToken(@RequestBody JwtRequest jwtRequest) {
        String token = authService.createAuthToken(jwtRequest);
        return ResponseEntity.ok(new JwtResponse(token));
    }
}
