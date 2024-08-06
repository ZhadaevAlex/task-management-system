package ru.zhadaev.taskmanagementsystem.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.zhadaev.taskmanagementsystem.dto.CreateUpdateUserDto;
import ru.zhadaev.taskmanagementsystem.dto.UserDto;
import ru.zhadaev.taskmanagementsystem.service.TaskService;
import ru.zhadaev.taskmanagementsystem.service.UserService;
import ru.zhadaev.taskmanagementsystem.validation.Marker;

import java.util.List;
import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;
    private final TaskService taskService;

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    @Validated(Marker.OnPost.class)
    public UserDto save(@RequestBody @Valid CreateUpdateUserDto createUpdateUserDto) {
        return userService.save(createUpdateUserDto);
    }

    @GetMapping("/{id}")
    public UserDto findById(@PathVariable("id") UUID id) {
        return userService.findById(id);
    }

    @GetMapping()
    public List<UserDto> findAll(Pageable pageable) {
        return userService.findAll(pageable);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public UserDto update(@RequestBody @Valid CreateUpdateUserDto createUpdateUserDto, @PathVariable("id") UUID id) {
        return userService.update(createUpdateUserDto, id);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable("id") UUID id) {
        userService.deleteById(id);
    }

    @DeleteMapping()
    public void deleteAll() {
        userService.deleteAll();
    }
}
