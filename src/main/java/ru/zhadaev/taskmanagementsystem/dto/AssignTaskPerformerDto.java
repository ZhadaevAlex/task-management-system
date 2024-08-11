package ru.zhadaev.taskmanagementsystem.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "Task performer")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AssignTaskPerformerDto {
    @NotBlank(message = "The performer's email must contain at least one non-whitespace character")
    @Email(message = "The performer's email address must be in the format user@example.com")
    @Size(max = 254, message = "performer's email should not exceed 254 characters")
    @Schema(description = "performer's email", example = "example@mail.com", required = true)
    private String performerEmail;
}
