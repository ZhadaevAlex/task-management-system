package ru.zhadaev.taskmanagementsystem.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Authorization controller", description = "Provides an endpoint for creating a JWT token")
public class AuthController {
    private final AuthService authService;

    @PostMapping
    @Operation(summary = "Create JWT token", description = "This endpoint creates a JWT token for the email and password specified in the request.")
    public ResponseEntity<?> createAuthToken(@RequestBody JwtRequest jwtRequest) {
        String token = authService.createAuthToken(jwtRequest);
        return ResponseEntity.ok(new JwtResponse(token));
    }
}
