package com.lucasavs.cryptoexchange.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "assets")
public class Asset {

    @Column(name = "symbol", unique = true, nullable = false)
    private String symbol;

    @Column(name = "kind", nullable = false)
    private String kind;

    @Column(name = "scale", nullable = false)
    private int scale;

    @Column(name = "name", nullable = false)
    private String name;

    // === constructors ===
    public Asset() {
    }
    public Asset(String symbol, String kind, int scale, String name) {
        this.symbol = symbol;
        this.kind = kind;
        this.scale = scale;
        this.name = name;
    }

    // === getters/setters ===
    public String getSymbol() {
        return symbol;
    }
    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }
    public String getKind() {
        return kind;
    }
    public void setKind(String kind) {
        this.kind = kind;
    }
    public int getScale() {
        return scale;
    }
    public void setScale(int scale) {
        this.scale = scale;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Asset{" +
                "symbol='" + symbol + '\'' +
                ", kind='" + kind + '\'' +
                ", scale=" + scale +
                ", name='" + name + '\'' +
                '}';
    }
}
