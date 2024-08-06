package ru.zhadaev.taskmanagementsystem.dto;

import jakarta.validation.constraints.NotNull;

public class AssignTaskPerformerDto {
    @NotNull(message = "The task's performer must be not null")
    private UserDto performer;
}
