package com.lucasavs.cryptoexchange.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public class UserCreateRequest {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(accessMode = Schema.AccessMode.READ_ONLY, description = "UUID")
    private UUID id;

    @NotBlank(message = "email is required")
    @Email(message = "invalid email")
    @Size(max = 254, message = "email must have a maximum of 254 characters")
    private String email;

    // write-only
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Schema(accessMode = Schema.AccessMode.WRITE_ONLY)
    @NotBlank(message = "password is required")
    @Size(min = 8, max = 72, message = "password must be between 8 and 72 characters long")
    private String password;

    public UUID getId() {
        return id;
    }
    public void setId(UUID id) {
        this.id = id;
    }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; } // para Service ler
    public void setPassword(String password) { this.password = password; }
}
