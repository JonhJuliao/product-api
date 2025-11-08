package com.example.product_api.service;

import com.example.product_api.domain.Product;
import com.example.product_api.dto.ProductRequest;
import com.example.product_api.dto.ProductResponse;
import com.example.product_api.repository.ProductRepository;
import com.example.product_api.service.exception.ProductNotFoundException;
import jakarta.transaction.Transactional;
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

    @Transactional
    public ProductResponse save(ProductRequest request) {
        var save = productRepository.save(new Product(request.name(), request.category()));
        return new ProductResponse(save.getId(), request.name(), request.category());
    }

    @Transactional
    public void delete(Long id) {
        if(!productRepository.existsById(id)){
            throw new ProductNotFoundException(id);
        }
        productRepository.deleteById(id);
    }
}
