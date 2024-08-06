package ru.zhadaev.taskmanagementsystem.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateUpdateCommentDto {
    @NotNull(message = "The comment's content must be not null")
    @Size(max = 4096, message = "The maximum number of characters is 4096")
    private String content;
}
