package com.example.product_api.service;

import com.example.product_api.domain.Product;
import com.example.product_api.dto.ProductResponse;
import com.example.product_api.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<ProductResponse> findAll() {
        return productRepository.findAll().stream()
                .map(p -> new ProductResponse(p.getId(), p.getName(), p.getCategory()))
                .toList();
    }
}
