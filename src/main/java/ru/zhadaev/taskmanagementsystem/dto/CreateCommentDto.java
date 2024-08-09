package ru.zhadaev.taskmanagementsystem.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Schema(description = "Updated comment")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateCommentDto {
    @NotNull(message = "The comment's content must be not null")
    @Size(max = 4096, message = "The maximum number of characters is 4096")
    private String content;

    @NotNull(message = "The task's ID must be not null")
    private UUID taskId;
}
