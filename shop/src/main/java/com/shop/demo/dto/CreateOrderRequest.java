package com.shop.demo.dto;

import java.math.BigDecimal;

public record CreateOrderRequest(Long productId, Integer quantity) {
}
