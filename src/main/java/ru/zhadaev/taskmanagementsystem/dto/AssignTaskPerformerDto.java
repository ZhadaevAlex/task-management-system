package ru.zhadaev.taskmanagementsystem.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "Task performer")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AssignTaskPerformerDto {
    @NotNull(message = "The task's performer must be not null")
    private UserDto performer;
}
