package dev.francode.microservice.product.controller;

import dev.francode.microservice.product.dto.ProductRequestDTO;
import dev.francode.microservice.product.dto.ProductResponseDTO;
import dev.francode.microservice.product.service.interfaces.ProductService;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> getProductById(@PathVariable Long id) {
        return productService.getProductById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ProductResponseDTO> createProduct(@Valid @RequestBody ProductRequestDTO productRequest) {
        ProductResponseDTO createdProduct = productService.createProduct(productRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdProduct);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> updateProduct(@PathVariable Long id,
                                                            @Valid @RequestBody ProductRequestDTO productRequest) {
        ProductResponseDTO updatedProduct = productService.updateProduct(id, productRequest);
        return ResponseEntity.ok(updatedProduct);
    }

    @GetMapping
    public ResponseEntity<Page<ProductResponseDTO>> getAllProducts(
            @ParameterObject
            @PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.DESC)
            Pageable pageable) {
        Page<ProductResponseDTO> page = productService.getAllProducts(pageable);
        return ResponseEntity.ok(page);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public ResponseEntity<List<ProductResponseDTO>> searchProductsByName(@RequestParam String name) {
        List<ProductResponseDTO> products = productService.searchProductsByName(name);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/available")
    public ResponseEntity<List<ProductResponseDTO>> getAvailableProducts() {
        List<ProductResponseDTO> products = productService.getAvailableProducts();
        return ResponseEntity.ok(products);
    }

    @GetMapping("/out-of-stock")
    public ResponseEntity<List<ProductResponseDTO>> getOutOfStockProducts() {
        List<ProductResponseDTO> products = productService.getOutOfStockProducts();
        return ResponseEntity.ok(products);
    }

    @GetMapping("/price/less-than")
    public ResponseEntity<List<ProductResponseDTO>> getProductsCheaperThan(@RequestParam BigDecimal price) {
        List<ProductResponseDTO> products = productService.getProductsCheaperThan(price);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/price/more-than")
    public ResponseEntity<List<ProductResponseDTO>> getProductsMoreExpensiveThan(@RequestParam BigDecimal price) {
        List<ProductResponseDTO> products = productService.getProductsMoreExpensiveThan(price);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/price/between")
    public ResponseEntity<List<ProductResponseDTO>> getProductsByPriceRange(@RequestParam BigDecimal minPrice,
                                                                            @RequestParam BigDecimal maxPrice) {
        List<ProductResponseDTO> products = productService.getProductsByPriceRange(minPrice, maxPrice);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/count")
    public ResponseEntity<Long> countAllProducts() {
        long count = productService.countAllProducts();
        return ResponseEntity.ok(count);
    }

    @GetMapping("/count/available")
    public ResponseEntity<Long> countAvailableProducts() {
        long count = productService.countAvailableProducts();
        return ResponseEntity.ok(count);
    }

    @GetMapping("/count/search")
    public ResponseEntity<Long> countProductsByName(@RequestParam String name) {
        long count = productService.countProductsByName(name);
        return ResponseEntity.ok(count);
    }

    @GetMapping("/most-expensive")
    public ResponseEntity<ProductResponseDTO> getMostExpensiveProduct() {
        return productService.getMostExpensiveProduct()
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/cheapest")
    public ResponseEntity<ProductResponseDTO> getCheapestProduct() {
        return productService.getCheapestProduct()
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}