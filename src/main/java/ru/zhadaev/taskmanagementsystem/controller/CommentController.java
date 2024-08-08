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
import ru.zhadaev.taskmanagementsystem.dto.CreateUpdateCommentDto;
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
    @Validated(Marker.OnPost.class)
    @SecurityRequirement(name = "JWT")
    @Operation(summary = "Add a new comment to the task with the specified ID", description = "This endpoint saves a new comment to the database for the the task with the specified ID and returns the comment object with the assigned ID")
    public CommentDto addToTask(@RequestBody @Valid CreateUpdateCommentDto createUpdateCommentDto, UUID taskId) {
        return commentService.addToTask(createUpdateCommentDto, taskId);
    }

    @GetMapping("/author/{id}")
    @SecurityRequirement(name = "JWT")
    @Operation(summary = "Retrieve all comments of a specific author of the task with the specified ID", description = "This endpoint retrieves a comments from the database of a specific author of the task with the specified ID")
    public List<CommentDto> findAllByAuthorByTaskId(@PathVariable("id") @Parameter(description = "Task ID") Pageable pageable, UUID taskId) {
        return commentService.findAllByAuthorByTaskId(pageable, taskId);
    }

    @GetMapping()
    @SecurityRequirement(name = "JWT")
    @Operation(summary = "Retrieve all comments of the task with the specified ID", description = "This endpoint returns a list of all comments stored in the database of the task with the specified ID")
    public List<CommentDto> findAllByTaskId(Pageable pageable, UUID taskId) {
        return commentService.findAllByTaskId(pageable, taskId);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @SecurityRequirement(name = "JWT")
    @Operation(summary = "Updating the comment by ID if the user is the author", description = "This endpoint updates the details of an existing comment in the database with the specified ID, but only if the requesting user is the author of the comment")
    public CommentDto updateByIdByAuthor(@RequestBody @Valid CreateUpdateCommentDto createUpdateCommentDto, @PathVariable("id") @Parameter(description = "Comment ID") UUID id) {
        return commentService.updateByIdByAuthor(createUpdateCommentDto, id);
    }

    @DeleteMapping("/{id}")
    @SecurityRequirement(name = "JWT")
    @Operation(summary = "Delete the comment by ID if the user is the author", description = "This endpoint deletes a comment in the database with the specified ID, but only if the requesting user is the author of the comment")
    public void deleteByIdByAuthor(@PathVariable("id") @Parameter(description = "Comment ID") UUID id) {
        commentService.deleteByIdByAuthor(id);
    }
}
