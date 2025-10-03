package com.lucasavs.cryptoexchange.controller;

import com.lucasavs.cryptoexchange.dto.*;
import com.lucasavs.cryptoexchange.security.SecurityConfiguration;
import com.lucasavs.cryptoexchange.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    @Operation(summary = "Authenticates a user and returns a JWT token")
    @ResponseStatus(HttpStatus.OK)
    public UserLoginResponse login(@RequestBody @Valid UserLoginRequest request) {
        String token = userService.login(request);
        return new UserLoginResponse(token);
    }

    @PostMapping("/save")
    @Operation(summary = "Save a new user in the system")
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto addUser(@Valid @RequestBody UserCreateRequest request) {
        return userService.save(request);
    }

    @GetMapping
    @Operation(summary = "Lists all users in the system", security = @SecurityRequirement(name = SecurityConfiguration.SECURITY))
    @ResponseStatus(HttpStatus.OK)
    public List<UserDto> findAll() {
        return userService.findAll();
    }

    @GetMapping("/{userId}")
    @Operation(summary = "Finds a user by their ID", security = @SecurityRequirement(name = SecurityConfiguration.SECURITY))
    @ResponseStatus(HttpStatus.OK)
    public UserDto getUser(@PathVariable UUID userId) {
        return userService.findById(userId);
    }

    @PatchMapping("/{userId}")
    @Operation(summary = "Updates a user's information", security = @SecurityRequirement(name = SecurityConfiguration.SECURITY))
    @ResponseStatus(HttpStatus.OK)
    public UserDto patchUpdate(
            @PathVariable UUID userId,
            @Valid @RequestBody UserUpdateRequest userUpdateRequest) {
        return userService.update(userId, userUpdateRequest);
    }

    @DeleteMapping("/{userId}")
    @Operation(summary = "Deletes a user by their ID", security = @SecurityRequirement(name = SecurityConfiguration.SECURITY))
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable UUID userId) {
        userService.deleteById(userId);
    }
}
