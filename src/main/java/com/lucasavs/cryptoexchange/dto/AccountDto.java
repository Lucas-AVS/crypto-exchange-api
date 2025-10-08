package com.lucasavs.cryptoexchange.dto;

import java.math.BigDecimal;
import java.util.UUID;

public class AccountDto {
    private UUID id;
    private UUID userId;
    private String assetSymbol;
    private BigDecimal balance;

    // Getters and Setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public UUID getUserId() { return userId; }
    public void setUserId(UUID userId) { this.userId = userId; }
    public String getAssetSymbol() { return assetSymbol; }
    public void setAssetSymbol(String assetSymbol) { this.assetSymbol = assetSymbol; }
    public BigDecimal getBalance() { return balance; }
    public void setBalance(BigDecimal balance) { this.balance = balance; }
}