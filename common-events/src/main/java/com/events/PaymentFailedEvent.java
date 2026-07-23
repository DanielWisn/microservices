package com.events;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class PaymentFailedEvent {
    private final UUID orderId;
    private final String reason;
    private final Instant failedAt;
}
