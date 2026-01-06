package com.example.demo.service;

import com.example.demo.model.Product;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductService {

    private List<Product> products = new ArrayList<>();
    private Long nextId = 1L;

    // get all the products
    public List<Product> getAllProducts() {
        return products;
    }

    // add a product
    public Product addProduct(Product product) {
        product.setId(nextId++);
        products.add(product);
        return product;
    }

    // get product by id
    public Product getProductById(Long id) {
        return products.stream().filter(p -> p.getId().equals(id)).findFirst().orElse(null);
    }

    // update product
    public Product updateProduct(Long id, Product newProduct) {
        Product product = getProductById(id);
        if (product != null) {
            product.setName(newProduct.getName());
            product.setPrice(newProduct.getPrice());
        }
        return product;
    }

    // delete product
    public void deleteProduct(Long id) {
        products.removeIf(p -> p.getId().equals(id));
    }
}
