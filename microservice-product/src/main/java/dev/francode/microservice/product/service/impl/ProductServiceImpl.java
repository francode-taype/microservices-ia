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

import java.util.Optional;

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

    private void validateId(Long id) {
        if (id == null || id <= 0) {
            throw new InvalidIdException("Invalid product ID: " + id);
        }
    }
}
