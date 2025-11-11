package com.lucasavs.wallet.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public class AccountCreateRequest {
    @NotBlank
    private String assetSymbol;

    @Positive(message = "balance must be positive")
    private BigDecimal balance;

    // Getters and Setters
    public String getAssetSymbol() { return assetSymbol; }
    public void setAssetSymbol(String assetSymbol) { this.assetSymbol = assetSymbol; }

    public BigDecimal getBalance() { return balance; }
    public void setBalance(BigDecimal balance) { this.balance = balance; }
}