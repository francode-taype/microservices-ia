package dev.francode.microservice.product.repository;

import dev.francode.microservice.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    // Find products whose name contains the given string (case-insensitive)
    List<Product> findByNameContainingIgnoreCase(String name);

    // Find products that have stock available (stock > 0)
    List<Product> findByStockGreaterThan(int stock);

    // Find products that are out of stock (stock == 0)
    List<Product> findByStockEquals(int stock);

    // Find products cheaper than a given price
    List<Product> findByPriceLessThan(BigDecimal price);

    // Find products more expensive than a given price
    List<Product> findByPriceGreaterThan(BigDecimal price);

    // Find products within a specific price range
    List<Product> findByPriceBetween(BigDecimal min, BigDecimal max);

    // Count how many products match a partial name (case-insensitive)
    long countByNameContainingIgnoreCase(String name);

    // Count how many products are in stock (stock > 0)
    long countByStockGreaterThan(int stock);

    // Count all products
    long count();

    // Get the most expensive product
    List<Product> findTop1ByOrderByPriceDesc();

    // Get the cheapest product
    List<Product> findTop1ByOrderByPriceAsc();
}

