package com.example.clothingstore.controller;

import com.example.clothingstore.entity.Product;
import com.example.clothingstore.entity.ProductVariant;
import com.example.clothingstore.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    // =========================
    // LIST PRODUCTS
    // =========================
    @GetMapping
    public String list(Model model) {
        model.addAttribute("products",
                productService.getAllActiveProducts());
        return "product/list";
    }

    // =========================
    // PRODUCT DETAIL
    // =========================
    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model) {

        Product product = productService.getProductById(id);
        if (product == null) {
            return "redirect:/products";
        }

        List<ProductVariant> variants =
                productService.getVariantsByProduct(id);

        if (variants == null) {
            variants = List.of(); // avoid 500
        }

        BigDecimal minPrice = BigDecimal.ZERO;
        BigDecimal maxPrice = BigDecimal.ZERO;

        if (!variants.isEmpty()) {
            minPrice = variants.stream()
                    .map(ProductVariant::getPrice)
                    .min(BigDecimal::compareTo)
                    .orElse(BigDecimal.ZERO);

            maxPrice = variants.stream()
                    .map(ProductVariant::getPrice)
                    .max(BigDecimal::compareTo)
                    .orElse(BigDecimal.ZERO);
        }

        model.addAttribute("product", product);
        model.addAttribute("variants", variants);
        model.addAttribute("minPrice", minPrice);
        model.addAttribute("maxPrice", maxPrice);

        return "product/detail";
    }

}
