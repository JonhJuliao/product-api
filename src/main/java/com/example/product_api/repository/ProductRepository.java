package com.example.product_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.product_api.domain.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {}
