package com.example.PDV.ProductsCore;

import com.example.PDV.ProductsCore.ProductDtos.ProductEntryDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/product")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {

        this.productService = productService;
    }

    @PostMapping("/create")
    public ResponseEntity<String> createProduct(@RequestBody ProductEntryDto entry) {

        productService.createProduct(entry);

        return ResponseEntity.status(HttpStatus.CREATED).body("Produto Registrado " +
                "com " +
                "sucesso!");
    }

    @GetMapping("/products")
    public ResponseEntity<List<ProductEntity>> getProducts() {

        List<ProductEntity> products = productService.seeProducts();

        return ResponseEntity.ok().body(products);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateProduct(@RequestBody ProductEntryDto entry,
                                                @PathVariable Integer id) {

        productService.updateProduct(entry, id);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping("/delete/{id}")
    ResponseEntity<String> deleteProdutct(@PathVariable Integer id) {

        productService.deleteProduct(id);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
