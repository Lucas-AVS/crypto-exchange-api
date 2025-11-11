package com.lucasavs.wallet.event;

import java.math.BigDecimal;
import java.util.UUID;

public class OrderPlacedEvent {
    private UUID orderId;
    private UUID userId;
    private String baseAssetSymbol;
    private String quoteAssetSymbol;
    private String side;
    private BigDecimal amountBase;
    private BigDecimal targetPrice;

    public OrderPlacedEvent() {
    }

    public OrderPlacedEvent(UUID orderId, UUID userId, String baseAssetSymbol,
                            String quoteAssetSymbol, String side,
                            BigDecimal amountBase, BigDecimal targetPrice) {
        this.orderId = orderId;
        this.userId = userId;
        this.baseAssetSymbol = baseAssetSymbol;
        this.quoteAssetSymbol = quoteAssetSymbol;
        this.side = side;
        this.amountBase = amountBase;
        this.targetPrice = targetPrice;
    }

    public UUID getOrderId() {
        return orderId;
    }

    public void setOrderId(UUID orderId) {
        this.orderId = orderId;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public String getBaseAssetSymbol() {
        return baseAssetSymbol;
    }

    public void setBaseAssetSymbol(String baseAssetSymbol) {
        this.baseAssetSymbol = baseAssetSymbol;
    }

    public String getQuoteAssetSymbol() {
        return quoteAssetSymbol;
    }

    public void setQuoteAssetSymbol(String quoteAssetSymbol) {
        this.quoteAssetSymbol = quoteAssetSymbol;
    }

    public String getSide() {
        return side;
    }

    public void setSide(String side) {
        this.side = side;
    }

    public BigDecimal getAmountBase() {
        return amountBase;
    }

    public void setAmountBase(BigDecimal amountBase) {
        this.amountBase = amountBase;
    }

    public BigDecimal getTargetPrice() {
        return targetPrice;
    }

    public void setTargetPrice(BigDecimal targetPrice) {
        this.targetPrice = targetPrice;
    }
}
