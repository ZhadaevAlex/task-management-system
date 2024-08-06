package ru.zhadaev.taskmanagementsystem.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import ru.zhadaev.taskmanagementsystem.dao.entity.Status;

@Data
public class ChangeStatusTaskDto {
    @NotNull(message = "The task's status must be not null")
    private Status status;
}
