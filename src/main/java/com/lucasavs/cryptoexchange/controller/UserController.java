package com.lucasavs.cryptoexchange.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.lucasavs.cryptoexchange.dto.UserCreateDto;
import com.lucasavs.cryptoexchange.dto.UserDto;
import com.lucasavs.cryptoexchange.dto.UserUpdateDto;
import com.lucasavs.cryptoexchange.entity.User;
import com.lucasavs.cryptoexchange.service.UserService;
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

    // add mapping for POST /Users - add new User

    @PostMapping("/users")
    public ResponseEntity<UserCreateDto> addUser(@RequestBody User user) {

        UserCreateDto createdUser = userService.save(user);

        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    // PATCH
    @PatchMapping("/{userId}")
    public ResponseEntity<UserUpdateDto> patchUpdate(
            @PathVariable UUID userId,
            @RequestBody UserUpdateDto userUpdateDto) {

        UserUpdateDto updatedUser = userService.update(userId, userUpdateDto);
        return ResponseEntity.ok(updatedUser);
    }

    private User apply(Map<String, Object> patchPayload, User userToPatch) {

        // Convert User object to a JSON object node
        ObjectNode userNode = objectMapper.convertValue(userToPatch, ObjectNode.class);

        // Convert the patchPayload map to a JSON object node
        ObjectNode patchNode = objectMapper.convertValue(patchPayload, ObjectNode.class);

        // Merge the patch updates into the User node
        userNode.setAll(patchNode);

        return objectMapper.convertValue(userNode, User.class);
    }

    // add mapping for DELETE /Users/{UserId} - delete User

    @DeleteMapping("/users/{userId}")
    public String deleteUser(@PathVariable UUID userId) {

        UserDto userToDelete = userService.findById(userId);

        // throw exception if null

        if (userToDelete == null) {
            throw new RuntimeException("User id not found - " + userId);
        }

        userService.deleteById(userId);

        return "Deleted User id - " + userId;
    }

}