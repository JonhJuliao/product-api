package com.example.product_api.dto;

import jakarta.validation.constraints.NotBlank;

public record ProductRequest(
        @NotBlank String name,
        @NotBlank String category
) {
}
