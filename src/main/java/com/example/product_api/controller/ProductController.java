package com.example.product_api.controller;

import com.example.product_api.dto.ProductRequest;
import com.example.product_api.dto.ProductResponse;
import com.example.product_api.service.ProductService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/api/products")
public class ProductController {

  private final ProductService productService;

  public ProductController(ProductService productService) {
    this.productService = productService;
  }

  @GetMapping
  public List<ProductResponse> findAll() {
    return productService.findAll();
  }

  @PostMapping
  public ResponseEntity<ProductResponse> save(
      @RequestBody @Valid ProductRequest request, UriComponentsBuilder uriComponentsBuilder) {

    var createdProduct = productService.save(request);
    var uriLocation =
        uriComponentsBuilder.path("/api/products/{id}").buildAndExpand(createdProduct.id()).toUri();
    return ResponseEntity.created(uriLocation).body(createdProduct);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    productService.delete(id);
    return ResponseEntity.noContent().build();
  }
}
