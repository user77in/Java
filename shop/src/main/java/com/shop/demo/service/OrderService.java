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
    private final UserService userService;
    private final UserRepository userRepository;

    @Transactional
    public OrderResponse createOrder(CreateOrderRequest request) {

        if (request.quantity() == null || request.quantity() <= 0) {
            throw new BadRequestException("Quantity must be greater than zero");
        }
        User user = userRepository.findUserEntityById(request.userId());
        Product product = productService.reserveStock(request.productId(), request.quantity());
        BigDecimal totalPrice = product.getPrice().multiply(BigDecimal.valueOf(request.quantity()));
        Order order = new Order(user, product.getId(), product.getName(), request.quantity(), totalPrice);
        Order saved = orderRepository.save(order);
        return OrderResponse.from(saved); // create order response dto from order entity
    }

    public OrderResponse getOrder(Long id) {
        Order order = orderRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("order not found: " + id));
        return OrderResponse.from(order);
    }

    public List<OrderResponse> getOrdersByUser(Long userId) {
        userService.findUserEntityById(userId);
        return orderRepository.findByUser_Id(userId).stream().map(OrderResponse::from).toList();
    }

    @Transactional
    public OrderResponse cancelOrder(Long id) {
        Order order = orderRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("order not found: " + id));
        if (order.getStatus() == OrderStatus.CANCELLED) {
            throw new BadRequestException("order is already cancelled");
        }
        productService.restoreStock(order.getProductId(), order.getQuantity());
        order.setStatus(OrderStatus.CANCELLED); // no need to save again @Transactional tracks changes
        return OrderResponse.from(order);
    }


}
