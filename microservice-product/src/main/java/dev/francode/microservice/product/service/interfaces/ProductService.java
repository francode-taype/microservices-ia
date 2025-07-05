package dev.francode.microservice.product.service.interfaces;

import dev.francode.microservice.product.dto.ProductRequestDTO;
import dev.francode.microservice.product.dto.ProductResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductService {

    // Basic CRUD
    Optional<ProductResponseDTO> getProductById(Long id);
    ProductResponseDTO createProduct(ProductRequestDTO productRequest);
    ProductResponseDTO updateProduct(Long id, ProductRequestDTO productRequest);
    Page<ProductResponseDTO> getAllProducts(Pageable pageable);
    void deleteProduct(Long id);

    // Advanced query methods for chatbot

    // Find products by partial name (case-insensitive)
    List<ProductResponseDTO> searchProductsByName(String name);

    // Find available products (stock > 0)
    List<ProductResponseDTO> getAvailableProducts();

    // Find out-of-stock products
    List<ProductResponseDTO> getOutOfStockProducts();

    // Find products cheaper than a given price
    List<ProductResponseDTO> getProductsCheaperThan(BigDecimal price);

    // Find products more expensive than a given price
    List<ProductResponseDTO> getProductsMoreExpensiveThan(BigDecimal price);

    // Find products within a price range
    List<ProductResponseDTO> getProductsByPriceRange(BigDecimal minPrice, BigDecimal maxPrice);

    // Count all products
    long countAllProducts();

    // Count products with stock > 0
    long countAvailableProducts();

    // Count products by partial name
    long countProductsByName(String name);

    // Get the most expensive product
    Optional<ProductResponseDTO> getMostExpensiveProduct();

    // Get the cheapest product
    Optional<ProductResponseDTO> getCheapestProduct();
}
