package com.lucasavs.cryptoexchange.integration;

import com.lucasavs.cryptoexchange.dto.UserCreateRequest;
import com.lucasavs.cryptoexchange.dto.UserDto;
import com.lucasavs.cryptoexchange.dto.UserUpdateRequest;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import java.util.Collections;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

// TODO: This test fails due to a database incompatibility.
// The H2 test database does not auto-generate the 'id' (UUID) and 'createdAt' (timestamp)
// in the same way as the production PostgreSQL database, causing assertions to fail.
// SOLUTION: This test must be run against a real PostgreSQL instance using Testcontainers.
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserIntegrationTest {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @LocalServerPort
    private int localServerPort;

    @Test
    @Disabled("need test container")
    void shouldCreateGetAndUpdateUserSuccessfully() {
        // --- 1. CREATE the user ---
        String baseUrl = "http://localhost:" + localServerPort + "/api/users";

        UserCreateRequest createRequestDto = new UserCreateRequest();
        createRequestDto.setEmail("user-" + UUID.randomUUID() + "@test.com");
        createRequestDto.setPassword("a-strong-password-123");

        HttpEntity<UserCreateRequest> createRequestEntity = new HttpEntity<>(createRequestDto, getJsonHeaders());
        ResponseEntity<UserDto> createResponseEntity = testRestTemplate.postForEntity(baseUrl, createRequestEntity, UserDto.class);

        // Assert creation was successful
        assertEquals(HttpStatus.CREATED, createResponseEntity.getStatusCode());
        UserDto createdUser = createResponseEntity.getBody();
        assertNotNull(createdUser);
        assertNotNull(createdUser.getId(), "The ID from the created user should not be null");
        assertEquals(createRequestDto.getEmail(), createdUser.getEmail());

        UUID userId = createdUser.getId();


        // --- 2. UPDATE the user ---
        String userUrl = baseUrl + "/" + userId;
        String newEmail = "updated-email-" + UUID.randomUUID() + "@test.com";

        UserUpdateRequest patchRequestDto = new UserUpdateRequest();
        patchRequestDto.setEmail(newEmail);

        HttpEntity<UserUpdateRequest> patchRequestEntity = new HttpEntity<>(patchRequestDto, getJsonHeaders());

        ResponseEntity<UserDto> patchResponseEntity = testRestTemplate.exchange(
                userUrl,
                HttpMethod.PATCH,
                patchRequestEntity,
                UserDto.class
        );

        // Assert update was successful
        assertEquals(HttpStatus.OK, patchResponseEntity.getStatusCode());
        assertNotNull(patchResponseEntity.getBody());
        assertEquals(newEmail, patchResponseEntity.getBody().getEmail());


        // --- 3. VERIFY the update by getting the user again ---
        ResponseEntity<UserDto> verifyResponseEntity = testRestTemplate.getForEntity(userUrl, UserDto.class);

        // Assert that the persisted data is correct
        assertEquals(HttpStatus.OK, verifyResponseEntity.getStatusCode());
        assertNotNull(verifyResponseEntity.getBody());
        assertEquals(newEmail, verifyResponseEntity.getBody().getEmail(), "The email should be updated after getting the user again");
    }

    private HttpHeaders getJsonHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        return headers;
    }
}