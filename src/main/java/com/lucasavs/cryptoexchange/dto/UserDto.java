package com.lucasavs.cryptoexchange.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

public class UserDto{
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(accessMode = Schema.AccessMode.READ_ONLY, description = "UUID")
    private UUID id;

    private String email;

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public UUID getId() {
        return id;
    }
    public void setId(UUID id) {
        this.id = id;
    }
}
