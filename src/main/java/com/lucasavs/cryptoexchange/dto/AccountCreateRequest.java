package com.lucasavs.cryptoexchange.dto;

import jakarta.validation.constraints.NotBlank;

public class AccountCreateRequest {
    @NotBlank
    private String assetSymbol;

    // Getters and Setters
    public String getAssetSymbol() { return assetSymbol; }
    public void setAssetSymbol(String assetSymbol) { this.assetSymbol = assetSymbol; }
}