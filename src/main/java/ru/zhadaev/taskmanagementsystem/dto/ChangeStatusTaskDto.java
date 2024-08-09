package ru.zhadaev.taskmanagementsystem.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.zhadaev.taskmanagementsystem.dao.entity.Status;

@Schema(description = "Task status")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChangeStatusTaskDto {
    @NotNull(message = "The task's status must be not null")
    private Status status;
}
