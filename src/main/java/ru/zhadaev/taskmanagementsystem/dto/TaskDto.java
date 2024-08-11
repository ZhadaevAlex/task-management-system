package ru.zhadaev.taskmanagementsystem.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.zhadaev.taskmanagementsystem.dao.entity.Status;
import java.util.UUID;

@Schema(description = "Task")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskDto {
    private UUID id;
    private String Header;
    private String description;
    private Status status;
    private Priority priority;
    private UserDto author;
    private UserDto performer;
//    private List<CommentDto> comments;
}
