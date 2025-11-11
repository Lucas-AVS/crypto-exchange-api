package com.lucasavs.wallet.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public class AccountUpdateRequest {
    @NotNull
    @Positive(message = "balance must be positive")
    private BigDecimal balance;

    // Getters and Setters
    public BigDecimal getBalance() { return balance; }
    public void setBalance(BigDecimal balance) { this.balance = balance; }
}