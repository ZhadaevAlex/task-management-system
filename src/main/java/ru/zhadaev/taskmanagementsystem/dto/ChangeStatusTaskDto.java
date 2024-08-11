package ru.zhadaev.taskmanagementsystem.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.zhadaev.taskmanagementsystem.validation.AllowValuesStatus;

@Schema(description = "Task status")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChangeStatusTaskDto {
    @NotBlank(message = "The status must contain at least one non-whitespace character")
    @AllowValuesStatus(message = "The status can take the following values: OPENED, IN_PROGRESS, COMPLETED")
    private String status;
}
