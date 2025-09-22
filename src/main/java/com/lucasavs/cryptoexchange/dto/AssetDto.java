package com.lucasavs.cryptoexchange.dto;

public class AssetDto {
    private String symbol;
    private String name;
    private String type; // 'kind' renamed to 'type'
    private int decimalPlaces; // 'scale' renamed to 'decimalPlaces'

    public String getSymbol() {
        return symbol;
    }
    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }

    public int getDecimalPlaces() {
        return decimalPlaces;
    }
    public void setDecimalPlaces(int decimalPlaces) {
        this.decimalPlaces = decimalPlaces;
    }
}
