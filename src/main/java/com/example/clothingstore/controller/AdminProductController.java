package com.example.clothingstore.controller;

import com.example.clothingstore.entity.Product;
import com.example.clothingstore.repository.BrandRepository;
import com.example.clothingstore.repository.CategoryRepository;
import com.example.clothingstore.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/products")
public class AdminProductController {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final BrandRepository brandRepository;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("products", productRepository.findAll());
        return "admin/product-list";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("product", new Product());
        model.addAttribute("categories", categoryRepository.findAll());
        model.addAttribute("brands", brandRepository.findAll());
        return "admin/product-form";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute Product product) {
        productRepository.save(product);
        return "redirect:/admin/products";
    }
}
