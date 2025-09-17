package com.lucasavs.cryptoexchange.dto;

import java.util.UUID;

public class UserDto{
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
