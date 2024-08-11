package ru.zhadaev.taskmanagementsystem.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.zhadaev.taskmanagementsystem.validation.AllowValuesPriority;
import ru.zhadaev.taskmanagementsystem.validation.AllowValuesStatus;
import ru.zhadaev.taskmanagementsystem.validation.Marker;
import ru.zhadaev.taskmanagementsystem.validation.NotNullBlankPath;

@Schema(description = "Created/updated task")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateUpdateTaskDto {
    @NotNullBlankPath(groups = Marker.Updated.class,
            message = "The header must contain at least one non-whitespace character. Can be null")
    @NotBlank(groups = Marker.Created.class, message = "The header must contain at least one non-whitespace character")
    private String header;

    @NotNullBlankPath(groups = Marker.Updated.class,
            message = "The description must contain at least one non-whitespace character. Can be null")
    @NotBlank(groups = Marker.Created.class, message = "The description must contain at least one non-whitespace character")
    private String description;

    @NotNullBlankPath(groups = Marker.Updated.class,
            message = "The status must contain at least one non-whitespace character. Can be null")
    @NotBlank(groups = Marker.Created.class, message = "The status must contain at least one non-whitespace character")
    @AllowValuesStatus(groups = {Marker.Created.class, Marker.Updated.class}, message = "The status can take the following values: OPENED, IN_PROGRESS, COMPLETED")
    private String status;

    @NotNullBlankPath(groups = Marker.Updated.class,
            message = "The priority must contain at least one non-whitespace character. Can be null")
    @NotBlank(groups = Marker.Created.class, message = "The status must contain at least one non-whitespace character")
    @AllowValuesPriority(groups = {Marker.Created.class, Marker.Updated.class}, message = "The priority can take the following values: HIGH, MEDIUM, LOW")
    private String priority;

    @NotNullBlankPath(groups = Marker.Updated.class,
            message = "The performer's email must contain at least one non-whitespace character. Can be null")
    @NotBlank(groups = Marker.Created.class, message = "The performer's email must contain at least one non-whitespace character")
    @Email(groups = {Marker.Created.class, Marker.Updated.class}, message = "The performer's email address must be in the format user@example.com")
    @Size(groups = {Marker.Created.class, Marker.Updated.class}, max = 254, message = "performer's email should not exceed 254 characters")
    @Schema(description = "performer's email", example = "example@mail.com", required = true)
    private String performerEmail;
}
