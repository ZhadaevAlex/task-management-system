package ru.zhadaev.taskmanagementsystem.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
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
public class CommentController {
    private final CommentService commentService;

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    @Validated(Marker.OnPost.class)
    public CommentDto save(@RequestBody @Valid CreateUpdateCommentDto createUpdateCommentDto, UUID taskId) {
        return commentService.addToTask(createUpdateCommentDto, taskId);
    }

    @GetMapping("/{id}")
    public CommentDto findById(@PathVariable("id") UUID id) {
        return commentService.findById(id);
    }

    @GetMapping()
    public List<CommentDto> findAll(Pageable pageable) {
        return commentService.findAll(pageable);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public CommentDto updateByAuthor(@RequestBody @Valid CreateUpdateCommentDto createUpdateCommentDto, @PathVariable("id") UUID id) {
        return commentService.updateByAuthor(createUpdateCommentDto, id);
    }

    @DeleteMapping("/{id}")
    public void deleteByIdByAuthor(@PathVariable("id") UUID id) {
        commentService.deleteByIdByAuthor(id);
    }

    @DeleteMapping()
    public void deleteAllByAuthor() {
        commentService.deleteAllByAuthor();
    }
}
