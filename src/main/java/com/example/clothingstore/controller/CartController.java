package com.example.clothingstore.controller;

import com.example.clothingstore.entity.User;
import com.example.clothingstore.repository.UserRepository;
import com.example.clothingstore.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/cart")
public class CartController {

    private final CartService cartService;
    private final UserRepository userRepository;

    @GetMapping
    public String viewCart(
            @AuthenticationPrincipal UserDetails userDetails,
            Model model
    ) {

        User user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        model.addAttribute("cartItems",
                cartService.getCartItems(user.getId()));
        model.addAttribute("total",
                cartService.calculateTotal(user.getId()));

        return "cart/cart";
    }

    @PostMapping("/add")
    public String addToCart(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam Long variantId,
            @RequestParam Integer quantity
    ) {

        User user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        cartService.addToCart(user, variantId, quantity);

        return "redirect:/cart";
    }

    @GetMapping("/remove")
    public String remove(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam Long variantId
    ) {

        User user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        cartService.removeFromCart(user.getId(), variantId);

        return "redirect:/cart";
    }

    @GetMapping("/clear")
    public String clear(
            @AuthenticationPrincipal UserDetails userDetails
    ) {

        User user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        cartService.clearCart(user.getId());

        return "redirect:/cart";
    }
}
