package ru.zhadaev.taskmanagementsystem.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.zhadaev.taskmanagementsystem.validation.Marker;

import java.util.UUID;

@Schema(description = "Updated comment")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateCommentDto {
    @NotNull(message = "The comment's content must be not null")
    @Size(max = 4096, message = "The maximum number of characters is 4096")
    private String content;

    @Pattern(regexp = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$",
            message = "It must be in the UUID format"
    )
    private String taskId;
}
