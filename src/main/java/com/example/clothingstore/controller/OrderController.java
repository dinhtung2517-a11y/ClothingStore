package com.example.clothingstore.controller;

import com.example.clothingstore.entity.Order;
import com.example.clothingstore.entity.User;
import com.example.clothingstore.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    private User mockUser() {
        User u = new User();
        u.setId(1L);
        return u;
    }

//    @PostMapping("/checkout")
//    public String checkout() {
//        orderService.checkout(mockUser());
//        return "redirect:/orders";
//    }

    @PostMapping("/checkout")
    public String checkout() {
        Order order = orderService.checkout(mockUser());
        return "redirect:/orders/success/" + order.getId();
    }

    @GetMapping("/success/{id}")
    public String success(@PathVariable Long id, Model model) {
        model.addAttribute("order", orderService.getOrderById(id));
        return "order/success";
    }


    @GetMapping
    public String list(Model model) {
        model.addAttribute("orders", orderService.getOrdersByUser(1L));
        return "order/list";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model) {

        var order = orderService.getOrderById(id);

        if (order == null) {
            return "redirect:/orders";
        }

        model.addAttribute("order", order);
        return "order/detail";
    }
}
