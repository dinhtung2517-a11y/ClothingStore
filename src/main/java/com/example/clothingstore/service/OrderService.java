package com.example.clothingstore.service;

import com.example.clothingstore.entity.*;
import com.example.clothingstore.repository.CartItemRepository;
import com.example.clothingstore.repository.OrderRepository;
import com.example.clothingstore.repository.ProductVariantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductVariantRepository variantRepository;

    // ===============================
    // CREATE ORDER FROM CART (CHECKOUT)
    // ===============================
    @Transactional
    public Order createOrderFromCart(User user) {

        List<CartItem> cartItems = cartItemRepository.findByUserId(user.getId());

        if (cartItems.isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }

        Order order = new Order();
        order.setUser(user);
        order.setStatus("PENDING");

        BigDecimal total = BigDecimal.ZERO;

        for (CartItem item : cartItems) {

            ProductVariant variant = item.getVariant();

            if (variant.getStock() < item.getQuantity()) {
                throw new RuntimeException(
                        "Not enough stock for " + variant.getProduct().getName()
                );
            }

            // ↓ decrease stock
            variant.setStock(variant.getStock() - item.getQuantity());
            variantRepository.save(variant);

            // ↓ create order detail
            OrderDetail detail = new OrderDetail();
            detail.setOrder(order);
            detail.setVariant(variant);
            detail.setQuantity(item.getQuantity());
            detail.setPrice(variant.getPrice());

            order.getOrderDetails().add(detail);

            total = total.add(
                    variant.getPrice()
                            .multiply(BigDecimal.valueOf(item.getQuantity()))
            );
        }

        order.setTotalPrice(total);

        Order savedOrder = orderRepository.save(order);

        // clear cart
        cartItemRepository.deleteByUserId(user.getId());

        return savedOrder;
    }

    // ===============================
    // GET ALL ORDERS BY USER
    // ===============================
    public List<Order> getOrdersByUser(Long userId) {
        return orderRepository.findByUserId(userId);
    }

    // ===============================
    // GET ORDER DETAIL (SECURE)
    // ===============================
    public Order getOrderByIdAndUser(Long orderId, Long userId) {
        return orderRepository.findByIdAndUserId(orderId, userId)
                .orElse(null);
    }

    public Order getOrderById(Long orderId) {
        return orderRepository.findById(orderId).orElse(null);
    }

}
