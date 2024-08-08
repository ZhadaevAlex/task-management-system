package ru.zhadaev.taskmanagementsystem.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Schema(description = "Comment")
@Data
public class CommentDto {
    private UUID id;
    private LocalDateTime time;
    private UserDto author;
    private String content;
}
