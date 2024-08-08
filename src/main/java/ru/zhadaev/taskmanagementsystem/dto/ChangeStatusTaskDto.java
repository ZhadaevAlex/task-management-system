package ru.zhadaev.taskmanagementsystem.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import ru.zhadaev.taskmanagementsystem.dao.entity.Status;

@Schema(description = "Task status")
@Data
public class ChangeStatusTaskDto {
    @NotNull(message = "The task's status must be not null")
    private Status status;
}
