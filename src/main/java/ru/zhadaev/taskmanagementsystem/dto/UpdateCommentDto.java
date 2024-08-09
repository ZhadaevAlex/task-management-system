package ru.zhadaev.taskmanagementsystem.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Schema(description = "Updated comment")
@Data
public class UpdateCommentDto {
    @NotNull(message = "The comment's content must be not null")
    @Size(max = 4096, message = "The maximum number of characters is 4096")
    private String content;
}
