package ru.zhadaev.taskmanagementsystem.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class CommentDto {
    private UUID id;
    private LocalDateTime time;
    private UserDto author;
    private String content;
}
