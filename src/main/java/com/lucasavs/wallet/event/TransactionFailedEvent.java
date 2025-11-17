package com.lucasavs.wallet.event;

import java.util.UUID;

public record TransactionFailedEvent(
        UUID orderId,
        String reason
) {}
