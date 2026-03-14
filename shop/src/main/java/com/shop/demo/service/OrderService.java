package com.shop.demo.service;

import com.shop.demo.dto.CreateOrderRequest;
import com.shop.demo.dto.OrderResponse;
import com.shop.demo.model.Order;
import com.shop.demo.model.OrderStatus;
import com.shop.demo.model.Product;
import com.shop.demo.repository.OrderRepository;
import com.shop.demo.repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final ProductService productService;

    @Transactional
    public OrderResponse createOrder(CreateOrderRequest request) {

        if (request.quantity() == null || request.quantity() <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than zero");
        }
        if (request.pricePerItem() == null || request.pricePerItem() <= 0) {
            throw new IllegalArgumentException("Price must be greater than zero");
        }
        Product product = productService.reserveStock(request.productId(), request.quantity());
        BigDecimal totalPrice = product.getPrice().multiply(BigDecimal.valueOf(request.quantity()));
        Order order = new Order(request.userId(), product.getName(), request.quantity(), totalPrice);
        Order saved = orderRepository.save(order);
        return OrderResponse.from(saved); // create order response dto from order entity
    }

    public OrderResponse getOrder(Long id) {
        Order order = orderRepository.findById(id).orElseThrow(() -> new RuntimeException("order not found: " + id));
        return OrderResponse.from(order);
    }

    public List<OrderResponse> getOrdersByUser(Long userId) {
        return orderRepository.findByUserId(userId).stream().map(OrderResponse::from).toList();
    }

    @Transactional
    public OrderResponse cancelOrder(Long id) {
        Order order = orderRepository.findById(id).orElseThrow(() -> new RuntimeException("order not found: " + id));
        if (order.getStatus() == OrderStatus.CANCELLED) {
            throw new IllegalArgumentException("order is already cancelled");
        }
        order.setStatus(OrderStatus.CANCELLED); // no need to save again @Transactional tracks changes
        return OrderResponse.from(order);
    }


}
