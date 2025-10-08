package com.lucasavs.cryptoexchange.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public class AccountUpdateRequest {
    @NotNull
    @DecimalMin("0.0")
    private BigDecimal balance;

    // Getters and Setters
    public BigDecimal getBalance() { return balance; }
    public void setBalance(BigDecimal balance) { this.balance = balance; }
}