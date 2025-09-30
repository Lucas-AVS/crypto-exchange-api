package com.lucasavs.cryptoexchange.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lucasavs.cryptoexchange.dto.UserCreateRequest;
import com.lucasavs.cryptoexchange.dto.UserDto;
import com.lucasavs.cryptoexchange.dto.UserUpdateRequest;
import com.lucasavs.cryptoexchange.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
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

    // Create User Tests

    @Test
    void shouldCreateUserWhenDetailsAreValid() throws Exception {
        // Arrange
        UserCreateRequest requestBody = createValidUserCreateRequest();
        UserDto userServiceResponse = createUserServiceResponse(UUID.randomUUID(), requestBody.getEmail());

        when(userService.save(any(UserCreateRequest.class))).thenReturn(userServiceResponse);

        // Act
        ResultActions resultActions = mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestBody)));

        // Assert
        resultActions
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(userServiceResponse.getId().toString()))
                .andExpect(jsonPath("$.email").value(requestBody.getEmail()))
                .andExpect(jsonPath("$.password").doesNotExist());
    }

    @ParameterizedTest
    @CsvSource({
            "invalid-email, aValidPassword123, invalid email",
            ", aValidPassword123, email is required",
            "test@test.com,, password is required",
            "a@b, aValidPassword123, email must be between 5 and 254 characters",
            "test@test.com, 1234567, password must be between 8 and 72 characters long",
    })
    void shouldReturnBadRequestWhenCreationDataIsInvalid(String email, String password, String expectedMessage) throws Exception {
        // Arrange
        UserCreateRequest requestBody = new UserCreateRequest();
        requestBody.setEmail(email);
        requestBody.setPassword(password);

        // Act & Assert
        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestBody)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(expectedMessage));
    }

    // Patch User Tests

    @Test
    void shouldUpdateUserWhenDetailsAreValid() throws Exception {
        // Arrange
        UUID userId = UUID.randomUUID();
        UserUpdateRequest requestBody = new UserUpdateRequest();
        requestBody.setEmail("new.email@test.com");

        UserDto userServiceResponse = createUserServiceResponse(userId, requestBody.getEmail());

        when(userService.update(any(UUID.class), any(UserUpdateRequest.class))).thenReturn(userServiceResponse);

        // Act
        ResultActions resultActions = mockMvc.perform(patch("/api/users/" + userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestBody)));

        // Assert
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userId.toString()))
                .andExpect(jsonPath("$.email").value(requestBody.getEmail()))
                .andExpect(jsonPath("$.password").doesNotExist());
    }

    @ParameterizedTest
    @CsvSource({
            "invalid-email, aValidPassword123, invalid email",
            "a@b, aValidPassword123, email must be between 5 and 254 characters",
            "new.email@test.com, 1234567, password must be between 8 and 72 characters long"
    })
    void shouldReturnBadRequestWhenUpdateDataIsInvalid(String email, String password, String expectedMessage) throws Exception {
        // Arrange
        UUID userId = UUID.randomUUID();
        UserUpdateRequest requestBody = new UserUpdateRequest();
        requestBody.setEmail(email);
        requestBody.setPassword(password);

        // Act & Assert
        mockMvc.perform(patch("/api/users/" + userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestBody)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(expectedMessage));
    }

    // Helper methods
    private UserCreateRequest createValidUserCreateRequest() {
        UserCreateRequest request = new UserCreateRequest();
        request.setEmail("test@test.com");
        request.setPassword("aValidPassword123");
        return request;
    }

    private UserDto createUserServiceResponse(UUID id, String email) {
        UserDto response = new UserDto();
        response.setId(id);
        response.setEmail(email);
        return response;
    }
}