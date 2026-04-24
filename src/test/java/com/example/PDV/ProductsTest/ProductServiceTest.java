package com.example.PDV.ProductsTest;

import com.example.PDV.Exceptions.ProductNotFound;
import com.example.PDV.LogsCore.ActivityLogsService;
import com.example.PDV.ProductsCore.ProductDtos.ProductEntryDto;
import com.example.PDV.ProductsCore.ProductEntity;
import com.example.PDV.ProductsCore.ProductRepository;
import com.example.PDV.ProductsCore.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.*;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    ProductRepository productRepository;

    @Mock
    ActivityLogsService activityLogsService;

    @InjectMocks
    ProductService productService;

    private ProductEntryDto dto;

    private ProductEntity product;

    @BeforeEach
    void setUp() {

        dto = new ProductEntryDto();
        dto.setProductName("product-test");
        dto.setQuantity(100);
        dto.setValue(new BigDecimal(300));

        product = new ProductEntity(dto);
        product.setId(1);
    }

    @Test
    void shouldCreateProduct() {

        productService.createProduct(dto);

        verify(productRepository).save(any());

        verify(activityLogsService).createActivityLogs(any());
    }

    @Test
    void shouldUpdateProduct() {

        when(productRepository.findById(1)).thenReturn(Optional.of(product));

        BigDecimal newValue = new BigDecimal(30);

        dto.setValue(newValue);

        productService.updateProduct(dto, 1);

        verify(productRepository).save(argThat(pro ->
                pro.getValue().equals(newValue)));

        verify(activityLogsService).createActivityLogs(any());
    }

    @Test
    void shouldNotUpdateProductNameWhenEmptyOrNull() {

        when(productRepository.findById(1)).thenReturn(Optional.of(product));

        String originalName = product.getProductName();
        dto.setProductName("  ");

        productService.updateProduct(dto, 1);

        verify(productRepository).save(argThat(pro ->
                pro.getProductName().equals(originalName)));
    }

    @Test
    void shouldNotUpdateQuantityWhenNull() {

        when(productRepository.findById(1)).thenReturn(Optional.of(product));

        Integer originalQuantity = product.getQuantity();
        dto.setQuantity(null);

        productService.updateProduct(dto, 1);

        verify(productRepository).save(argThat(pro ->
                pro.getQuantity().equals(originalQuantity)));
    }

    @Test
    void shouldNotUpdateProductValueWhenNull() {

        when(productRepository.findById(1)).thenReturn(Optional.of(product));

        BigDecimal originalValue = product.getValue();
        dto.setValue(null);

        productService.updateProduct(dto, 1);

        verify(productRepository).save(argThat(pro ->
                pro.getValue().equals(originalValue)));
    }

    @Test
    void shouldDeleteProduct() {

        when(productRepository.findById(1)).thenReturn(Optional.of(product));

        productService.deleteProduct(1);

        verify(productRepository).delete(product);

        verify(activityLogsService).createActivityLogs(any());
    }

    @Test
    void shouldThrowProductNotFound() {

        when(productRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(ProductNotFound.class, () -> {
            productService.deleteProduct(1);
        });
    }
}
