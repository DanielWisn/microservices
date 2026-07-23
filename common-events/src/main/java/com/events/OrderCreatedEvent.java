package com.events;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class OrderCreatedEvent {
    private final UUID orderId;
    private final UUID userId;
    private final String userEmail;
    private final List<OrderItem> items;
    private final BigDecimal totalAmount;
    private final Instant createdAt;

    @Getter
    @AllArgsConstructor
    public static class OrderItem {
        private final UUID productId;
        private final String productName;
        private final int quantity;
        private final BigDecimal unitPrice;
    }
}