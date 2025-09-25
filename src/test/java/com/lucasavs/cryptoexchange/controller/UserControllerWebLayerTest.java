package com.lucasavs.cryptoexchange.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lucasavs.cryptoexchange.dto.UserCreateRequest;
import com.lucasavs.cryptoexchange.dto.UserDto;
import com.lucasavs.cryptoexchange.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(
        controllers = UserController.class,
        excludeAutoConfiguration = {SecurityAutoConfiguration.class}
)
public class UserControllerWebLayerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private UserService userService;

    @Test
    @DisplayName("User can be created when valid user details are provided")
    void testCreateUser_whenValidUserDetailsProvided_returnsUserDTO() throws Exception {
        // Arrange
        UserCreateRequest requestBody = new UserCreateRequest();
        requestBody.setEmail("test@test.com");
        requestBody.setPassword("aValidPassword123");

        UserDto userServiceResponse = new UserDto();
        userServiceResponse.setId(UUID.randomUUID());
        userServiceResponse.setEmail(requestBody.getEmail());

        // Mock the service layer
        when(userService.save(any(UserCreateRequest.class)))
                .thenReturn(userServiceResponse);

        // Act
        ResultActions resultActions = mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestBody))); // Use the DTO directly

        // Assert
        resultActions
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.email").value(requestBody.getEmail()))
                .andExpect(jsonPath("$.password").doesNotExist()); // Important: verify password is not in the response
    }

    @Test
    @DisplayName("Should return 400 Bad Request when Email is empty")
    void createUser_whenEmailIsEmpty_returns400StatusCode() throws Exception {
        // Arrange
        UserCreateRequest requestBody = new UserCreateRequest();
        requestBody.setEmail("test@test.com");
        requestBody.setPassword("aValidPassword123");

        // Act & Assert
        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(requestBody)))
                .andExpect(status().isBadRequest()) // Check if the status is 400 (Bad Request)
                .andExpect(jsonPath("$.message").value("email is required"))
                .andExpect(jsonPath("$.password").doesNotExist());
    }
}