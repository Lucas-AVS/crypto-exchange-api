package com.lucasavs.wallet.controller;

import com.lucasavs.wallet.dto.*;
import com.lucasavs.wallet.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/wallet")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/auth/login")
    @Operation(summary = "Authenticates a user and returns a JWT token")
    @ResponseStatus(HttpStatus.OK)
    public UserLoginResponse login(@RequestBody @Valid UserLoginRequest request) {
        String token = userService.login(request);
        return new UserLoginResponse(token);
    }

    @PostMapping("/auth/save")
    @Operation(summary = "Save a new user in the system")
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto addUser(@Valid @RequestBody UserCreateRequest request) {
        return userService.save(request);
    }

    @GetMapping("/users")
    @ResponseStatus(HttpStatus.OK)
    public List<UserDto> findAll() {
        return userService.findAll();
    }

    @GetMapping("/users/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public UserDto getUser(@PathVariable UUID userId) {
        return userService.findById(userId);
    }

    @PatchMapping("/users/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public UserDto patchUpdate(
            @PathVariable UUID userId,
            @Valid @RequestBody UserUpdateRequest userUpdateRequest) {
        return userService.update(userId, userUpdateRequest);
    }

    @DeleteMapping("/users/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable UUID userId) {
        userService.deleteById(userId);
    }
}
