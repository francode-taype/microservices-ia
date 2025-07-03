package dev.francode.microservice.product.mapper;

import dev.francode.microservice.product.dto.ProductRequestDTO;
import dev.francode.microservice.product.dto.ProductResponseDTO;
import dev.francode.microservice.product.entity.Product;

public class ProductMapper {

    public static Product toEntity(ProductRequestDTO dto) {
        if (dto == null) {
            return null;
        }
        Product product = new Product();
        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setStock(dto.getStock());
        product.setPrice(dto.getPrice());
        return product;
    }

    public static ProductResponseDTO toResponseDto(Product entity) {
        if (entity == null) {
            return null;
        }
        ProductResponseDTO dto = new ProductResponseDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());
        dto.setStock(entity.getStock());
        dto.setPrice(entity.getPrice());
        return dto;
    }
}