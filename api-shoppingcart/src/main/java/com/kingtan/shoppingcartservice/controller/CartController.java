package com.kingtan.shoppingcartservice.controller;

import com.kingtan.shoppingcartservice.model.CartItem;
import com.kingtan.shoppingcartservice.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    // Get all items in the cart
    @GetMapping
    public List<CartItem> getAllItems() {
        return cartService.getAllItems();
    }

    // Add an item to the cart
    @PostMapping
    public CartItem addItem(@RequestBody CartItem item) {
        return cartService.addItem(item);
    }

    // Get a specific item by ID
    @GetMapping("/{id}")
    public ResponseEntity<CartItem> getItem(@PathVariable Long id) {
        return cartService.getItem(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Update an item in the cart
    @PutMapping("/{id}")
    public CartItem updateItem(@PathVariable Long id, @RequestBody CartItem item) {
        return cartService.updateItem(id, item);
    }

    // Remove an item from the cart
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteItem(@PathVariable Long id) {
        cartService.deleteItem(id);
        return ResponseEntity.noContent().build();
    }
}