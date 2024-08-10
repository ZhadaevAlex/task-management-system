package ru.zhadaev.taskmanagementsystem.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.zhadaev.taskmanagementsystem.validation.Marker;
import ru.zhadaev.taskmanagementsystem.validation.NotNullBlankPath;

@Data
@Schema(description = "Created/updated user")
@AllArgsConstructor
@NoArgsConstructor
public class CreateUpdateUserDto {
    @NotNullBlankPath(groups = Marker.Updated.class,
            message = "The email address must be not null")
    @NotBlank(groups = Marker.Created.class, message = "The email must contain at least one non-whitespace character")
    @Email(groups = {Marker.Created.class, Marker.Updated.class}, message = "The email address must be in the format user@example.com")
    @Size(groups = {Marker.Created.class, Marker.Updated.class}, max = 254, message = "Email should not exceed 254 characters")
    @Schema(description = "email", example = "example@mail.com", required = true)
    private String email;

    @NotNullBlankPath(groups = Marker.Updated.class,
            message = "The email address must be not null")
    @NotBlank(groups = Marker.Created.class, message = "The password must contain at least one non-whitespace character")
    @Size(groups = {Marker.Created.class, Marker.Updated.class}, min = 8, max = 128, message = "The password must consist of at least 8 characters and no more than 128 characters")
    @Pattern(groups = {Marker.Created.class, Marker.Updated.class},
            regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=]).*$",
            message = "Password must contain at least one digit, one lowercase letter, one uppercase letter, and one special character (@#$%^&+=)"
    )
    private String password;
}
