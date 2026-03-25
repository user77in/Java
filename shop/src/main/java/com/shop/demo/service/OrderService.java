package com.shop.demo.service;

import com.shop.demo.dto.CreateOrderRequest;
import com.shop.demo.dto.OrderResponse;
import com.shop.demo.exception.BadRequestException;
import com.shop.demo.exception.ResourceNotFoundException;
import com.shop.demo.model.Order;
import com.shop.demo.model.OrderStatus;
import com.shop.demo.model.Product;
import com.shop.demo.model.User;
import com.shop.demo.repository.OrderRepository;
import com.shop.demo.repository.ProductRepository;
import com.shop.demo.repository.UserRepository;
import com.shop.demo.security.SecurityUtils;
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
    public OrderResponse createOrder(CreateOrderRequest request, User currentUser) {
        if (request.quantity() == null || request.quantity() <= 0) {
            throw new BadRequestException("Quantity must be greater than zero");
        }

        Product product = productService.reserveStock(request.productId(), request.quantity());
        var totalPrice = product.getPrice()
                .multiply(BigDecimal.valueOf(request.quantity()));

        Order order = new Order(currentUser, product.getId(),
                product.getName(), request.quantity(), totalPrice);
        return OrderResponse.from(orderRepository.save(order));
    }

    public OrderResponse getOrder(Long orderId, User currentUser) {
        Order order = getOrderAndVerifyOwnership(orderId, currentUser.getId());
        return OrderResponse.from(order);
    }

    public List<OrderResponse> getMyOrders(User currentUser) {
        return orderRepository.findByUser_Id(currentUser.getId())
                .stream()
                .map(OrderResponse::from)
                .toList();
    }

    @Transactional
    public OrderResponse cancelOrder(Long orderId, User currentUser) {
        Order order = getOrderAndVerifyOwnership(orderId, currentUser.getId());

        if (order.getStatus() == OrderStatus.CANCELLED) {
            throw new BadRequestException("Order is already cancelled");
        }

        productService.restoreStock(order.getProductId(), order.getQuantity());
        order.setStatus(OrderStatus.CANCELLED);
        return OrderResponse.from(order);
    }

    private Order getOrderAndVerifyOwnership(Long orderId, Long userId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found: " + orderId));

        if (!order.getUser().getId().equals(userId)) {
            throw new BadRequestException("You can only access your own orders");
        }

        return order;
    }
}