package com.events;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class PaymentSucceededEvent {
    private final UUID orderId;
    private final UUID paymentId;
    private final BigDecimal amount;
    private final String paymentMethod;
    private final Instant processedAt;
}