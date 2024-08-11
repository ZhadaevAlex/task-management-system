package ru.zhadaev.taskmanagementsystem.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Schema(description = "JWT Token")
@Data
@AllArgsConstructor
public class JwtResponse {
    private String token;
}
