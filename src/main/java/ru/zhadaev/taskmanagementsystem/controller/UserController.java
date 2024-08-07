package ru.zhadaev.taskmanagementsystem.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.zhadaev.taskmanagementsystem.dto.CreateUpdateUserDto;
import ru.zhadaev.taskmanagementsystem.dto.UserDto;
import ru.zhadaev.taskmanagementsystem.service.UserService;
import ru.zhadaev.taskmanagementsystem.validation.Marker;

import java.util.List;
import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping("/api/users")
@Tag(name = "User controller", description = "This resource represents an all users in the system")
public class UserController {
    private final UserService userService;

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    @Validated(Marker.OnPost.class)
    @Operation(summary = "Add a new user", description = "This method adds a new user")
    public UserDto save(
            @RequestBody @Valid @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "New user") CreateUpdateUserDto createUpdateUserDto) {
        return userService.save(createUpdateUserDto);
    }

    @GetMapping("/{id}")
    public UserDto findById(@PathVariable("id") UUID id) {
        return userService.findById(id);
    }

    @GetMapping()
    @SecurityRequirement(name = "JWT")
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
