package com.lucasavs.wallet.event;

import java.math.BigDecimal;
import java.util.UUID;

public record OrderMatchedEvent(
        UUID orderId,
        UUID userId,
        String baseAssetSymbol,
        String quoteAssetSymbol,
        String side,
        BigDecimal amountBase,
        BigDecimal executedPrice
) {}
