package com.example.PDV.ProductsCore;

import com.example.PDV.Exceptions.ProductNotFound;
import com.example.PDV.LogsCore.ActivityLogsService;
import com.example.PDV.LogsCore.Dtos.ActivityLogsEntryDto;
import com.example.PDV.LogsCore.Enums.EntityType;
import com.example.PDV.LogsCore.Enums.TypeAction;
import com.example.PDV.ProductsCore.ProductDtos.ProductEntryDto;
import com.example.PDV.ProductsCore.ProductDtos.ProductsDto;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    private final ActivityLogsService activityLogsService;

    public ProductService(ProductRepository productRepository,
                          ActivityLogsService activityLogsService) {

        this.productRepository = productRepository;
        this.activityLogsService = activityLogsService;
    }

    public void createProduct(ProductEntryDto entry) {

        ProductEntity newProduct = new ProductEntity(entry);

        productRepository.save(newProduct);

        activityLogsService.createActivityLogs(new ActivityLogsEntryDto(EntityType.PRODUCT,
                newProduct.getId(), TypeAction.CREATE));
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

        ProductEntity newProduct = productRepository.findById(productId)
                .orElseThrow(ProductNotFound::new);

        newProduct.updateProduct(entry);

        productRepository.save(newProduct);

        activityLogsService.createActivityLogs(new ActivityLogsEntryDto(EntityType.PRODUCT,
                newProduct.getId(), TypeAction.UPDATE));
    }

    public void deleteProduct(Integer productId) {

        ProductEntity product = productRepository.findById(productId)
                        .orElseThrow(ProductNotFound::new);

        productRepository.delete(product);

        activityLogsService.createActivityLogs(new ActivityLogsEntryDto(EntityType.PRODUCT,
                product.getId(), TypeAction.DELETE));
    }

    @CacheEvict(
            value = "Products_To_Sale",
            key = "'ListOfProducts'"
    )
    public void evichProducts() {

    }
}
