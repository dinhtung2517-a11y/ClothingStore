package com.example.clothingstore.service;

import com.example.clothingstore.entity.Product;
import com.example.clothingstore.entity.ProductVariant;
import com.example.clothingstore.repository.ProductRepository;
import com.example.clothingstore.repository.ProductVariantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductVariantRepository productVariantRepository;

    public List<Product> getAllActiveProducts() {
        return productRepository.findByIsActiveTrue();
    }

    public Product getProductById(Long id) {
        return productRepository.findById(id).orElse(null);
    }

    @Transactional
    public List<ProductVariant> getVariantsByProduct(Long productId) {
        return productVariantRepository.findByProductId(productId);
    }

    public ProductVariant getVariantById(Long variantId) {
        return productVariantRepository.findById(variantId).orElse(null);
    }
}
