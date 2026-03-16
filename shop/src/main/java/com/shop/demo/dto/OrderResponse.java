package com.shop.demo.dto;

import com.shop.demo.model.Order;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record OrderResponse(Long id, UserResponse user, String productName, Integer quantity, BigDecimal totalPrice,
                            String status, LocalDateTime createdAt) {
    public static OrderResponse from(Order order) { // from method converts Entity -> DTO
        return new OrderResponse(order.getId(),
                UserResponse.from(order.getUser()),
                order.getProductName(),
                order.getQuantity(),
                order.getTotalPrice(),
                order.getStatus().name(),
                order.getCreatedAt());
    }

}
