package com.example.product_api.controller;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.example.product_api.dto.ProductRequest;
import com.example.product_api.dto.ProductResponse;
import com.example.product_api.service.ProductService;
import com.example.product_api.service.exception.ProductNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(ProductController.class)
class ProductControllerTest {

  @Autowired private MockMvc mvc;

  @Autowired private ObjectMapper objectMapper;

  @MockBean private ProductService productService;

  @Test
  @DisplayName("GET /api/products deve retornar 200 com lista de produtos")
  void findAll_ok() throws Exception {
    List<ProductResponse> list =
        List.of(
            new ProductResponse(1L, "Laptop", "Eletronic"),
            new ProductResponse(2L, "Mouse", "Periferic"));
    given(productService.findAll()).willReturn(list);

    mvc.perform(get("/api/products"))
        .andExpect(status().isOk())
        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$", hasSize(2)))
        .andExpect(jsonPath("$[0].id", is(1)))
        .andExpect(jsonPath("$[0].name", is("Laptop")))
        .andExpect(jsonPath("$[0].category", is("Eletronic")))
        .andExpect(jsonPath("$[1].id", is(2)));
  }

  @Test
  @DisplayName("POST /api/products deve retornar 201 com Location e corpo do criado")
  void save_created() throws Exception {
    ProductRequest req = new ProductRequest("Headset USB", "Periferic");
    ProductResponse created = new ProductResponse(10L, req.name(), req.category());
    given(productService.save(any(ProductRequest.class))).willReturn(created);

    mvc.perform(
            post("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
        .andExpect(status().isCreated())
        .andExpect(header().string("Location", endsWith("/api/products/10")))
        .andExpect(jsonPath("$.id", is(10)))
        .andExpect(jsonPath("$.name", is("Headset USB")))
        .andExpect(jsonPath("$.category", is("Periferic")));
  }

  @Test
  @DisplayName("POST /api/products com payload inválido deve retornar 400")
  void save_badRequest_whenInvalid() throws Exception {
    // Depende de ProductRequest ter @NotBlank nos campos
    String invalidJson = """
            { "name": "", "category": "" }
        """;

    mvc.perform(post("/api/products").contentType(MediaType.APPLICATION_JSON).content(invalidJson))
        .andExpect(status().isBadRequest());
  }

  @Test
  @DisplayName("DELETE /api/products/{id} deve retornar 204 quando existir")
  void delete_noContent_whenExists() throws Exception {
    doNothing().when(productService).delete(1L);

    mvc.perform(delete("/api/products/{id}", 1L)).andExpect(status().isNoContent());
  }

  @Test
  @DisplayName("DELETE /api/products/{id} deve retornar 404 quando não existir")
  void delete_notFound_whenMissing() throws Exception {
    doThrow(new ProductNotFoundException(999L)).when(productService).delete(999L);

    mvc.perform(delete("/api/products/{id}", 999L)).andExpect(status().isNotFound());
  }
}
