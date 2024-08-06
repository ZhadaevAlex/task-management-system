package ru.zhadaev.taskmanagementsystem.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import ru.zhadaev.taskmanagementsystem.validation.Marker;

@Data
public class CreateUpdateUserDto {
    @NotNull(groups = Marker.OnPost.class,
            message = "The email address must be not null")
    @NotBlank(message = "The email must contain at least one non-whitespace character")
    @Email(message = "The email address must be in the format user@example.com")
    @Size(max = 254, message = "Email should not exceed 254 characters")
    private String email;

    @NotNull(groups = Marker.OnPost.class,
            message = "The password must be not null")
    @NotBlank(message = "The password must contain at least one non-whitespace character")
    @Size(min = 8, max = 128, message = "The password must consist of at least 8 characters and no more than 128 characters")
    @Pattern(
            regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=]).*$",
            message = "Password must contain at least one digit, one lowercase letter, one uppercase letter, and one special character (@#$%^&+=)"
    )
    private String password;
}
