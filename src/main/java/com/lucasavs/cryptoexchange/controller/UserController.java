package com.lucasavs.cryptoexchange.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.lucasavs.cryptoexchange.dto.UserCreateRequest;
import com.lucasavs.cryptoexchange.dto.UserDto;
import com.lucasavs.cryptoexchange.dto.UserUpdateRequest;
import com.lucasavs.cryptoexchange.entity.User;
import com.lucasavs.cryptoexchange.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class UserController {

    private final UserService userService;
    private final ObjectMapper objectMapper;

    @Autowired
    public UserController(UserService userService, ObjectMapper objectMapper) {
        this.userService = userService;
        this.objectMapper = objectMapper;
    }

    // expose "/users" and return a list of Users
    @GetMapping("/users")
    public ResponseEntity<List<UserDto>> findAll() {
        return ResponseEntity.ok(userService.findAll());
    }

    // add mapping for GET /users/{userId}
    @GetMapping("/users/{userId}")
    public ResponseEntity<UserDto> getUser(@PathVariable UUID userId) {
        UserDto user = userService.findById(userId);
        if (user == null) {
            throw new RuntimeException("User id not found - " + userId);
        }
        return ResponseEntity.ok(user);
    }

    // add mapping for POST /users - add new User
    @PostMapping("/users")
    public ResponseEntity<UserDto> addUser(@Valid @RequestBody UserCreateRequest request) {
        UserDto created = userService.save(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    // PATCH
    @PatchMapping("/users/{userId}")
    public ResponseEntity<UserDto> patchUpdate(
            @PathVariable UUID userId,
            @Valid @RequestBody UserUpdateRequest userUpdateRequest) {

        UserDto updatedUser = userService.update(userId, userUpdateRequest);
        return ResponseEntity.ok(updatedUser);
    }

    private User apply(Map<String, Object> patchPayload, User userToPatch) {
        ObjectNode userNode = objectMapper.convertValue(userToPatch, ObjectNode.class);
        ObjectNode patchNode = objectMapper.convertValue(patchPayload, ObjectNode.class);
        userNode.setAll(patchNode);
        return objectMapper.convertValue(userNode, User.class);
    }

    // add mapping for DELETE /users/{userId} - delete User
    @DeleteMapping("/users/{userId}")
    public String deleteUser(@PathVariable UUID userId) {
        UserDto userToDelete = userService.findById(userId);
        if (userToDelete == null) {
            throw new RuntimeException("User id not found - " + userId);
        }
        userService.deleteById(userId);
        return "Deleted User id - " + userId;
    }
}