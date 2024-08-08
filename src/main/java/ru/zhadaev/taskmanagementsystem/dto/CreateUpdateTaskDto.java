package ru.zhadaev.taskmanagementsystem.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import ru.zhadaev.taskmanagementsystem.dao.entity.Status;
import ru.zhadaev.taskmanagementsystem.validation.Marker;

@Schema(description = "Created/updated task")
@Data
public class CreateUpdateTaskDto {
    @NotNull(groups = Marker.OnPost.class,
            message = "The header must be not null")
    private String Header;
    private String description;
    @NotNull(groups = Marker.OnPost.class,
            message = "The status must be not null")
    private Status status;
    @NotNull(groups = Marker.OnPost.class,
            message = "The priority must be not null")
    private Priority priority;
    @NotNull(groups = Marker.OnPost.class,
            message = "The performer must be not null")
    private UserDto performer;
}
