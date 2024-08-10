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
@Tag(name = "Task controller", description = "Provides endpoints for managing tasks in the system")
public class TaskController {
    private final TaskService taskService;

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    @SecurityRequirement(name = "JWT")
    @Operation(summary = "Add a new task to the database", description = "This endpoint saves a new task to the database and returns the task object with the assigned ID")
    public TaskDto save(@RequestBody @Validated(Marker.Created.class) @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "New task") CreateUpdateTaskDto createUpdateTaskDto) {
        return taskService.save(createUpdateTaskDto);
    }

    @GetMapping("/{id}")
    @SecurityRequirement(name = "JWT")
    @Operation(summary = "Find a task by ID", description = "This endpoint retrieves a task from the database using their unique ID")
    public TaskDto findById(@PathVariable("id") @Parameter(description = "Task ID") UUID id) {
        return taskService.findById(id);
    }

    @GetMapping()
    @SecurityRequirement(name = "JWT")
    @Operation(summary = "Retrieve all tasks", description = "This endpoint returns a list of all tasks stored in the database")
    public List<TaskDto> findAll(Pageable pageable) {
        return taskService.findAll(pageable);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @SecurityRequirement(name = "JWT")
    @Operation(summary = "Update a task if the user is the author", description = "This endpoint updates the details of an existing task in the database, but only if the requesting user is the author of the task")
    public TaskDto updateByAuthor(@RequestBody @Validated(Marker.Created.class) @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Data for update") CreateUpdateTaskDto createUpdateTaskDto, @Parameter(description = "Task ID") @PathVariable("id") UUID id) {
        return taskService.updateByAuthor(createUpdateTaskDto, id);
    }

    @PatchMapping("/status/{id}")
    @SecurityRequirement(name = "JWT")
    @Operation(summary = "Change the status of a task by ID", description = "This endpoint allows changing the status of a specific task identified by its unique ID. The request can be made only by the author of the task or the assigned performer")
    public TaskDto changeStatusById(@RequestBody @Valid ChangeStatusTaskDto changeStatusTaskDto, @PathVariable("id") @Parameter(description = "Task ID") UUID id) {
        return taskService.changeStatusById(changeStatusTaskDto, id);
    }

    @PatchMapping("/performer/{id}")
    @SecurityRequirement(name = "JWT")
    @Operation(summary = "Assign a performer to a task by ID if the user is the author", description = "This endpoint assigns a performer to a specific task identified by its unique ID, but only if the requesting user is the author of the task.")
    public TaskDto assignPerformerById(@RequestBody @Valid AssignTaskPerformerDto assignTaskPerformerDto, @PathVariable("id") @Parameter(description = "Task ID") UUID id) {
        return taskService.assignPerformerById(assignTaskPerformerDto, id);
    }

    @DeleteMapping("/{id}")
    @SecurityRequirement(name = "JWT")
    @Operation(summary = "Delete a task by ID if the user is the author", description = "This endpoint allows the deletion of a task identified by its unique ID, but only if the requesting user is the author of the task")
    public void deleteByIdByAuthor(@PathVariable("id") @Parameter(description = "Task ID") UUID id) {
        taskService.deleteByIdByAuthor(id);
    }

    @DeleteMapping()
    @SecurityRequirement(name = "JWT")
    @Operation(summary = "Delete all tasks by the author", description = "This endpoint allows the deletion of all tasks authored by a specific user")
    public void deleteAllByAuthor() {
        taskService.deleteAllByAuthor();
    }
}
