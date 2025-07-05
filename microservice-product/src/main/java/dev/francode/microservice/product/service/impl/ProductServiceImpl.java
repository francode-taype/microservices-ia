package dev.francode.microservice.product.service.impl;

import dev.francode.microservice.product.dto.ProductRequestDTO;
import dev.francode.microservice.product.dto.ProductResponseDTO;
import dev.francode.microservice.product.entity.Product;
import dev.francode.microservice.product.exception.InvalidIdException;
import dev.francode.microservice.product.exception.ProductNotFoundException;
import dev.francode.microservice.product.mapper.ProductMapper;
import dev.francode.microservice.product.repository.ProductRepository;
import dev.francode.microservice.product.service.interfaces.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public Optional<ProductResponseDTO> getProductById(Long id) {
        validateId(id);
        return productRepository.findById(id)
                .map(ProductMapper::toResponseDto);
    }

    @Override
    public ProductResponseDTO createProduct(ProductRequestDTO productRequest) {
        Product product = ProductMapper.toEntity(productRequest);
        Product savedProduct = productRepository.save(product);
        return ProductMapper.toResponseDto(savedProduct);
    }

    @Override
    public ProductResponseDTO updateProduct(Long id, ProductRequestDTO productRequest) {
        validateId(id);

        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with ID: " + id));

        existingProduct.setName(productRequest.getName());
        existingProduct.setDescription(productRequest.getDescription());
        existingProduct.setStock(productRequest.getStock());
        existingProduct.setPrice(productRequest.getPrice());

        Product updatedProduct = productRepository.save(existingProduct);
        return ProductMapper.toResponseDto(updatedProduct);
    }

    @Override
    public Page<ProductResponseDTO> getAllProducts(Pageable pageable) {
        return productRepository.findAll(pageable)
                .map(ProductMapper::toResponseDto);
    }

    @Override
    public void deleteProduct(Long id) {
        validateId(id);

        if (!productRepository.existsById(id)) {
            throw new ProductNotFoundException("Product not found with ID: " + id);
        }

        productRepository.deleteById(id);
    }

    @Override
    public List<ProductResponseDTO> searchProductsByName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Search name cannot be null or empty");
        }
        List<Product> products = productRepository.findByNameContainingIgnoreCase(name);
        return products.stream()
                .map(ProductMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductResponseDTO> getAvailableProducts() {
        List<Product> products = productRepository.findByStockGreaterThan(0);
        return products.stream()
                .map(ProductMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductResponseDTO> getOutOfStockProducts() {
        List<Product> products = productRepository.findByStockEquals(0);
        return products.stream()
                .map(ProductMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductResponseDTO> getProductsCheaperThan(BigDecimal price) {
        validatePrice(price);
        List<Product> products = productRepository.findByPriceLessThan(price);
        return products.stream()
                .map(ProductMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductResponseDTO> getProductsMoreExpensiveThan(BigDecimal price) {
        validatePrice(price);
        List<Product> products = productRepository.findByPriceGreaterThan(price);
        return products.stream()
                .map(ProductMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductResponseDTO> getProductsByPriceRange(BigDecimal minPrice, BigDecimal maxPrice) {
        validatePrice(minPrice);
        validatePrice(maxPrice);
        if (minPrice.compareTo(maxPrice) > 0) {
            throw new IllegalArgumentException("Min price cannot be greater than max price");
        }
        List<Product> products = productRepository.findByPriceBetween(minPrice, maxPrice);
        return products.stream()
                .map(ProductMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public long countAllProducts() {
        return productRepository.count();
    }

    @Override
    public long countAvailableProducts() {
        return productRepository.countByStockGreaterThan(0);
    }

    @Override
    public long countProductsByName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name for count cannot be null or empty");
        }
        return productRepository.countByNameContainingIgnoreCase(name);
    }

    @Override
    public Optional<ProductResponseDTO> getMostExpensiveProduct() {
        List<Product> products = productRepository.findTop1ByOrderByPriceDesc();
        return products.stream()
                .findFirst()
                .map(ProductMapper::toResponseDto);
    }

    @Override
    public Optional<ProductResponseDTO> getCheapestProduct() {
        List<Product> products = productRepository.findTop1ByOrderByPriceAsc();
        return products.stream()
                .findFirst()
                .map(ProductMapper::toResponseDto);
    }

    private void validatePrice(BigDecimal price) {
        if (price == null || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Price must be non-null and non-negative");
        }
    }

    private void validateId(Long id) {
        if (id == null || id <= 0) {
            throw new InvalidIdException("Invalid product ID: " + id);
        }
    }
}
