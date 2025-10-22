package com.lucasavs.wallet.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public class UserUpdateRequest {

    @Email(message = "invalid email")
    @Size(min = 5, max = 254, message = "email must be between 5 and 254 characters")
    private String email;

    @Size(min = 8, max = 72, message = "password must be between 8 and 72 characters long")
    private String password;

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}

