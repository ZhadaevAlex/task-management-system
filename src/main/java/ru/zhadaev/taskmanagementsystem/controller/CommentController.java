package ru.zhadaev.taskmanagementsystem.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.zhadaev.taskmanagementsystem.dto.CommentDto;
import ru.zhadaev.taskmanagementsystem.dto.CreateCommentDto;
import ru.zhadaev.taskmanagementsystem.dto.UpdateCommentDto;
import ru.zhadaev.taskmanagementsystem.service.CommentService;
import ru.zhadaev.taskmanagementsystem.validation.Marker;

import java.util.List;
import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping("/api/comments")
@Tag(name = "Comment controller", description = "Provides endpoints for managing comments in the system")
public class CommentController {
    private final CommentService commentService;

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    @Validated(Marker.Created.class)
    @SecurityRequirement(name = "JWT")
    @Operation(summary = "Add a new comment to the task with the specified ID", description = "This endpoint saves a new comment to the database for the the task with the specified ID and returns the comment object with the assigned ID")
    public CommentDto create(@RequestBody @Valid CreateCommentDto createCommentDto) {
        return commentService.addToTask(createCommentDto);
    }

    @GetMapping("/{taskId}")
    @SecurityRequirement(name = "JWT")
    @Operation(summary = "Retrieve all comments of the task with the specified ID", description = "This endpoint returns a list of all comments stored in the database of the task with the specified ID")
    public List<CommentDto> findAllByTaskId(@PathVariable("taskId") @Parameter(description = "Task ID") UUID taskId, Pageable pageable) {
        return commentService.findAllByTaskId(taskId, pageable);
    }

    @PatchMapping("/author/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @SecurityRequirement(name = "JWT")
    @Operation(summary = "Updating the comment by ID if the user is the author", description = "This endpoint updates the details of an existing comment in the database with the specified ID, but only if the requesting user is the author of the comment")
    public CommentDto updateByIdByAuthor(@RequestBody @Valid UpdateCommentDto updateCommentDto, @PathVariable("id") @Parameter(description = "Comment ID") UUID id) {
        return commentService.updateByIdByAuthor(updateCommentDto, id);
    }

    @DeleteMapping("/author/{id}")
    @SecurityRequirement(name = "JWT")
    @Operation(summary = "Delete the comment by ID if the user is the author", description = "This endpoint deletes a comment in the database with the specified ID, but only if the requesting user is the author of the comment")
    public void deleteByIdByAuthor(@PathVariable("id") @Parameter(description = "Comment ID") UUID id) {
        commentService.deleteByIdByAuthor(id);
    }
}
