package ru.zhadaev.taskmanagementsystem.dto;

import lombok.Data;
import ru.zhadaev.taskmanagementsystem.dao.entity.Status;
import java.util.List;
import java.util.UUID;

@Data
public class TaskDto {
    private UUID id;
    private String Header;
    private String description;
    private Status status;
    private Priority priority;
    private UserDto author;
    private UserDto performer;
    private List<CommentDto> comments;
}
