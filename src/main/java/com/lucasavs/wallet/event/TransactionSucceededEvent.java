package com.lucasavs.wallet.event;

import java.math.BigDecimal;
import java.util.UUID;

public record TransactionSucceededEvent(
        UUID orderId,
        BigDecimal executedPrice
) {}
