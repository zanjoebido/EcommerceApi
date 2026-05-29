package com.ws101.bido.EcommerceApi.controller;

import com.ws101.bido.EcommerceApi.model.Product;
import com.ws101.bido.EcommerceApi.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        return ResponseEntity.ok(productService.getAllProducts());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        Product product = productService.getProductById(id)
                .orElseThrow(() -> new IllegalArgumentException("Product tracking token ID " + id + " does not exist."));
        return ResponseEntity.ok(product);
    }

    @GetMapping("/filter")
    public ResponseEntity<List<Product>> filterProducts(
            @RequestParam String filterType,
            @RequestParam String filterValue) {
        return ResponseEntity.ok(productService.filterProducts(filterType, filterValue));
    }

    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {
        if (product.getName() == null || product.getName().trim().isEmpty()) {
            throw new RuntimeException("Validation Error: Product name is mandatory.");
        }
        if (product.getPrice() == null || product.getPrice() <= 0) {
            throw new RuntimeException("Validation Error: Product price must be a positive value.");
        }
        return new ResponseEntity<>(productService.createProduct(product), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @RequestBody Product product) {
        Product updated = productService.updateProduct(id, product)
                .orElseThrow(() -> new IllegalArgumentException("Cannot update; ID " + id + " not found."));
        return ResponseEntity.ok(updated);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Product> patchProduct(@PathVariable Long id, @RequestBody Product product) {
        Product patched = productService.patchProduct(id, product)
                .orElseThrow(() -> new IllegalArgumentException("Cannot patch modify; ID " + id + " not found."));
        return ResponseEntity.ok(patched);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        if (!productService.deleteProduct(id)) {
            throw new IllegalArgumentException("Cannot delete; Product tracking reference ID " + id + " not found.");
        }
        return ResponseEntity.noContent().build();
    }
}