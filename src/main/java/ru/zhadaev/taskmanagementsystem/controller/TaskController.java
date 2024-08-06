package ru.zhadaev.taskmanagementsystem.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.zhadaev.taskmanagementsystem.dto.AssignTaskPerformerDto;
import ru.zhadaev.taskmanagementsystem.dto.ChangeStatusTaskDto;
import ru.zhadaev.taskmanagementsystem.dto.CreateUpdateTaskDto;
import ru.zhadaev.taskmanagementsystem.dto.TaskDto;
import ru.zhadaev.taskmanagementsystem.service.TaskService;
import ru.zhadaev.taskmanagementsystem.validation.Marker;

import java.util.List;
import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping("/api/tasks")
public class TaskController {
    private final TaskService taskService;

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    @Validated(Marker.OnPost.class)
    public TaskDto save(@RequestBody @Valid CreateUpdateTaskDto createUpdateTaskDto) {
        return taskService.save(createUpdateTaskDto);
    }

    @GetMapping("/{id}")
    public TaskDto findById(@PathVariable("id") UUID id) {
        return taskService.findById(id);
    }

    @GetMapping()
    public List<TaskDto> findAll(Pageable pageable) {
        return taskService.findAll(pageable);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public TaskDto updateByAuthor(@RequestBody @Valid CreateUpdateTaskDto createUpdateTaskDto, @PathVariable("id") UUID id) {
        return taskService.updateByAuthor(createUpdateTaskDto, id);
    }

    @PatchMapping("/status/{id}")
    public TaskDto changeStatusById(@RequestBody @Valid ChangeStatusTaskDto changeStatusTaskDto, UUID id) {
        return taskService.changeStatusById(changeStatusTaskDto, id);
    }

    @PatchMapping("/performer/{id}")
    public TaskDto assignPerformerById(@RequestBody @Valid AssignTaskPerformerDto assignTaskPerformerDto, UUID id) {
        return taskService.assignPerformerById(assignTaskPerformerDto, id);
    }

    @DeleteMapping("/{id}")
    public void deleteByIdByAuthor(@PathVariable("id") UUID id) {
        taskService.deleteByIdByAuthor(id);
    }

    @DeleteMapping()
    public void deleteAllByAuthor() {
        taskService.deleteAllByAuthor();
    }
}
