package com.example.PDV.ProductsCore;

import com.example.PDV.Exceptions.ProductNotFound;
import com.example.PDV.ProductsCore.ProductDtos.ProductEntryDto;
import com.example.PDV.ProductsCore.ProductDtos.ProductsDto;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {

        this.productRepository = productRepository;
    }

    public void createProduct(ProductEntryDto entry) {

        ProductEntity newProduct = new ProductEntity(entry);

        productRepository.save(newProduct);
    }

    @Cacheable(
            value = "Products_To_Sale",
            key = "'ListOfProducts'",
            unless = "#result == null"
    )
    public List<ProductsDto> seeProducts() {

        return productRepository.findAll()
                .stream()
                .map(p -> new ProductsDto(
                        p.getId(),
                        p.getProductName(),
                        p.getQuantity(),
                        p.getValue()
                        )
                ).toList();
    }

    public void updateProduct(ProductEntryDto entry, Integer productId) {

        ProductEntity newProducto = productRepository.findById(productId)
                .orElseThrow(ProductNotFound::new);

        newProducto.updateProduct(entry);

        productRepository.save(newProducto);
    }

    public void deleteProduct(Integer productId) {

        ProductEntity product = productRepository.findById(productId)
                        .orElseThrow(ProductNotFound::new);

        productRepository.delete(product);
    }

    @CacheEvict(
            value = "Products_To_Sale",
            key = "'ListOfProducts'"
    )
    public void evichProducts() {

    }
}
