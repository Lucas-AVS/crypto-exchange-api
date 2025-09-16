package com.lucasavs.cryptoexchange.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.lucasavs.cryptoexchange.entity.User;
import com.lucasavs.cryptoexchange.service.UserService;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class UserController {

    private UserService userService;

    private ObjectMapper objectMapper;

    @Autowired
    public UserController(UserService theUserService, ObjectMapper theObjectMapper) {
        userService = theUserService;
        objectMapper = theObjectMapper;
    }

    // expose "/users" and return a list of Users
    @GetMapping("/users")
    public List<User> findAll() {
        return userService.findAll();
    }

    // add mapping for GET /users/{userId}

    @GetMapping("/users/{userId}")
    public User getUser(@PathVariable UUID userId) {

        User theUser = userService.findById(userId);

        if (theUser == null) {
            throw new RuntimeException("User id not found - " + userId);
        }

        return theUser;
    }

    // add mapping for POST /Users - add new User

    @PostMapping("/users")
    public User addUser(@RequestBody User theUser) {

//        ????????????????????
        // also just in case they pass an id in JSON ... set id to 0
        // this is to force a save of new item ... instead of update
        // theUser.setId(0);

        User dbUser = userService.save(theUser);

        return dbUser;
    }

    // add mapping for PUT /users - update existing User

    @PutMapping("/users/{userId}")
    public User updateUser(@PathVariable UUID userId, @RequestBody User body) throws ChangeSetPersister.NotFoundException {
        var existing = userService.findById(userId);
        if (existing == null) throw new ChangeSetPersister.NotFoundException();
        //TODO -> Exception handler!!!

        existing.setEmail(body.getEmail());
        existing.setPasswordHash(body.getPasswordHash());
        return userService.save(existing);
    }

    // add mapping for PATCH /users/{userId} - patch User ... partial update

    @PatchMapping("/users/{userId}")
    public User patchUser(@PathVariable UUID userId,
                                  @RequestBody Map<String, Object> patchPayload) {

        User tempUser = userService.findById(userId);

        // throw exception if null
        if (tempUser == null) {
            throw new RuntimeException("User id not found - " + userId);
        }

        // throw exception if request body contains "id" key
        if (patchPayload.containsKey("id")) {
            throw new RuntimeException("User id not allowed in request body - " + userId);
        }

        User patchedUser = apply(patchPayload, tempUser);

        User dbUser = userService.save(patchedUser);

        return dbUser;
    }

    private User apply(Map<String, Object> patchPayload, User tempUser) {

        // Convert User object to a JSON object node
        ObjectNode userNode = objectMapper.convertValue(tempUser, ObjectNode.class);

        // Convert the patchPayload map to a JSON object node
        ObjectNode patchNode = objectMapper.convertValue(patchPayload, ObjectNode.class);

        // Merge the patch updates into the User node
        userNode.setAll(patchNode);

        return objectMapper.convertValue(userNode, User.class);
    }

    // add mapping for DELETE /Users/{UserId} - delete User

    @DeleteMapping("/users/{userId}")
    public String deleteUser(@PathVariable UUID userId) {

        User tempUser = userService.findById(userId);

        // throw exception if null

        if (tempUser == null) {
            throw new RuntimeException("User id not found - " + userId);
        }

        userService.deleteById(userId);

        return "Deleted User id - " + userId;
    }

}