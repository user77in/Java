package com.shop.demo.service;

import com.shop.demo.dto.CreateProductRequest;
import com.shop.demo.dto.ProductResponse;
import com.shop.demo.model.Product;
import com.shop.demo.repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    @Transactional
    public ProductResponse createProduct(CreateProductRequest request) {
        if (request.name() == null || request.name().isBlank()) {
            throw new IllegalArgumentException("Product name cannot be blank");
        }
        if (request.price() == null || request.price() <= 0) {
            throw new IllegalArgumentException("Price must be greater than zero");
        }
        if (request.stockQuantity() == null || request.stockQuantity() < 0) {
            throw new IllegalArgumentException("Stock cannot be negative");
        }
        if (productRepository.existsByName(request.name())) {
            throw new IllegalArgumentException("Product already exists" + request.name());
        }
        Product product = new Product(request.name(), BigDecimal.valueOf(request.price()), request.stockQuantity());
        return ProductResponse.from(productRepository.save(product));
    }

    public ProductResponse getProduct(Long id) {
        Product product = productRepository.findById(id).orElseThrow(() -> new RuntimeException("product not found " + id));
        return ProductResponse.from(product);
    }

    public List<ProductResponse> getAllProducts() {
        return productRepository.findAll().stream().map(ProductResponse::from).toList();
    }

    @Transactional
    public Product reserveStock(Long productId, Integer quantity) {
        Product product = productRepository.findById(productId).orElseThrow(() -> new RuntimeException("product not found" + productId));
        if (product.getStockQuantity() < quantity) {
            throw new IllegalArgumentException(
                    "Not enough stock for: " + product.getName() + ". Requested: " + quantity + ", Available: " + product.getStockQuantity()
            );
        }
        product.setStockQuantity(product.getStockQuantity() - quantity);
        return productRepository.save(product);
    }
}
