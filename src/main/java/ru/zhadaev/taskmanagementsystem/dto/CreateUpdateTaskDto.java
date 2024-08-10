package ru.zhadaev.taskmanagementsystem.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.zhadaev.taskmanagementsystem.dao.entity.Status;
import ru.zhadaev.taskmanagementsystem.validation.Marker;

@Schema(description = "Created/updated task")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateUpdateTaskDto {
    @NotNull(groups = Marker.Created.class,
            message = "The header must be not null")
    private String Header;
    private String description;
    @NotNull(groups = Marker.Created.class,
            message = "The status must be not null")
    private Status status;
    @NotNull(groups = Marker.Created.class,
            message = "The priority must be not null")
    private Priority priority;
    private UserDto performer;
}
