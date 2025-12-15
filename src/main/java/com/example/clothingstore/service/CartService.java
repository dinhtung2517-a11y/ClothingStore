package com.example.clothingstore.service;

import com.example.clothingstore.entity.CartItem;
import com.example.clothingstore.entity.ProductVariant;
import com.example.clothingstore.entity.User;
import com.example.clothingstore.repository.CartItemRepository;
import com.example.clothingstore.repository.ProductVariantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartItemRepository cartItemRepository;
    private final ProductVariantRepository variantRepository;

    public List<CartItem> getCartItems(Long userId) {
        return cartItemRepository.findByUserId(userId);
    }

    public void addToCart(User user, Long variantId, Integer quantity) {

        ProductVariant variant = variantRepository.findById(variantId)
                .orElseThrow(() -> new RuntimeException("Variant not found"));

        CartItem cartItem = cartItemRepository
                .findByUserIdAndVariantId(user.getId(), variantId)
                .orElse(null);

        if (cartItem == null) {
            cartItem = new CartItem();
            cartItem.setUser(user);
            cartItem.setVariant(variant);
            cartItem.setQuantity(quantity);
        } else {
            cartItem.setQuantity(cartItem.getQuantity() + quantity);
        }

        cartItemRepository.save(cartItem);
    }

    public void removeFromCart(Long userId, Long variantId) {
        cartItemRepository.findByUserIdAndVariantId(userId, variantId)
                .ifPresent(cartItemRepository::delete);
    }

    public void clearCart(Long userId) {
        cartItemRepository.deleteByUserId(userId);
    }

    public BigDecimal calculateTotal(Long userId) {
        return cartItemRepository.findByUserId(userId).stream()
                .map(item ->
                        item.getVariant().getPrice()
                                .multiply(BigDecimal.valueOf(item.getQuantity()))
                )
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
