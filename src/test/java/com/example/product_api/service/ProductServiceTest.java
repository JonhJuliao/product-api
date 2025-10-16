package com.example.product_api.service;

import com.example.product_api.domain.Product;
import com.example.product_api.dto.ProductRequest;
import com.example.product_api.dto.ProductResponse;
import com.example.product_api.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService service;

    @Captor
    private ArgumentCaptor<Product> productCaptor;


    @Test
    void findAll_deveRetornarListaMapeadaParaResponse() {
        Product p1 = mock(Product.class);
        when(p1.getId()).thenReturn(10L);
        when(p1.getName()).thenReturn("Laptop");
        when(p1.getCategory()).thenReturn("Eletronic");

        Product p2 = mock(Product.class);
        when(p2.getId()).thenReturn(11L);
        when(p2.getName()).thenReturn("Mouse Gamer");
        when(p2.getCategory()).thenReturn("Periferic");

        when(productRepository.findAll()).thenReturn(List.of(p1, p2));

        List<ProductResponse> result = service.findAll();

        assertThat(result).hasSize(2);
        assertThat(result.get(0).id()).isEqualTo(10L);
        assertThat(result.get(0).name()).isEqualTo("Laptop");
        assertThat(result.get(0).category()).isEqualTo("Eletronic");

        assertThat(result.get(1).id()).isEqualTo(11L);
        assertThat(result.get(1).name()).isEqualTo("Mouse Gamer");
        assertThat(result.get(1).category()).isEqualTo("Periferic");

        verify(productRepository, times(1)).findAll();
        verifyNoMoreInteractions(productRepository);
    }

    @Test
    void save_deveSalvarEDevolverResponseComIdGerado() {
        ProductRequest req = new ProductRequest("Keyboard", "Periferic");

        Product saved = mock(Product.class);
        when(saved.getId()).thenReturn(42L);
        when(productRepository.save(any(Product.class))).thenReturn(saved);

        ProductResponse resp = service.save(req);

        assertThat(resp.id()).isEqualTo(42L);
        assertThat(resp.name()).isEqualTo("Keyboard");
        assertThat(resp.category()).isEqualTo("Periferic");

        verify(productRepository).save(productCaptor.capture());
        Product enviado = productCaptor.getValue();
        assertThat(enviado.getName()).isEqualTo("Keyboard");
        assertThat(enviado.getCategory()).isEqualTo("Periferic");

        verifyNoMoreInteractions(productRepository);
    }
}
