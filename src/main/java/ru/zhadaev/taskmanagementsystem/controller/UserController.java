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
import ru.zhadaev.taskmanagementsystem.dto.CreateUpdateUserDto;
import ru.zhadaev.taskmanagementsystem.dto.UserDto;
import ru.zhadaev.taskmanagementsystem.service.UserService;
import ru.zhadaev.taskmanagementsystem.validation.Marker;

import java.util.List;
import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping("/api/users")
@Tag(name = "User controller", description = "Provides endpoints for managing users in the system")
public class UserController {
    private final UserService userService;

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    @Validated(Marker.OnPost.class)
    @SecurityRequirement(name = "JWT")
    @Operation(summary = "Add a new user to the database", description = "This endpoint saves a new user to the database and returns the user object with the assigned id")
    public UserDto save(
            @RequestBody @Valid @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "New user") CreateUpdateUserDto createUpdateUserDto) {
        return userService.save(createUpdateUserDto);
    }

    @GetMapping("/{id}")
    @SecurityRequirement(name = "JWT")
    @Operation(summary = "Find a user by ID", description = "This endpoint retrieves a user from the database using their unique ID")
    public UserDto findById(@PathVariable("id") @Parameter(description = "User ID") UUID id) {
        return userService.findById(id);
    }

    @GetMapping("/{email}")
    @SecurityRequirement(name = "JWT")
    @Operation(summary = "Find a user by email", description = "This endpoint retrieves a user from the database using their unique email")
    public UserDto findByEmail(@PathVariable("email") @Parameter(description = "User email") String email) {
        return userService.findByEmail(email);
    }

    @GetMapping()
    @SecurityRequirement(name = "JWT")
    @Operation(summary = "Retrieve all users", description = "This endpoint returns a list of all users stored in the database")
    public List<UserDto> findAll(Pageable pageable) {
        return userService.findAll(pageable);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @SecurityRequirement(name = "JWT")
    @Operation(summary = "Update an existing user", description = "This endpoint updates the details of an existing user in the database")
    public UserDto update(@RequestBody @Valid @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Data for update") CreateUpdateUserDto createUpdateUserDto, @PathVariable("id") @Parameter(description = "User ID") UUID id) {
        return userService.update(createUpdateUserDto, id);
    }

    @DeleteMapping("/{id}")
    @SecurityRequirement(name = "JWT")
    @Operation(summary = "Delete a user by ID", description = "This endpoint deletes a user from the database using their unique ID")
    public void deleteById(@PathVariable("id") @Parameter(description = "User ID") UUID id) {
        userService.deleteById(id);
    }

    @DeleteMapping("/{email}")
    @SecurityRequirement(name = "JWT")
    @Operation(summary = "Delete a user by email", description = "This endpoint deletes a user from the database using their unique email")
    public void deleteByEmail(@PathVariable("email") @Parameter(description = "email") String email) {
        userService.deleteByEmail(email);
    }

    @DeleteMapping()
    @SecurityRequirement(name = "JWT")
    @Operation(summary = "Delete all users", description = "This endpoint deletes all users stored in the database")
    public void deleteAll() {
        userService.deleteAll();
    }
}
