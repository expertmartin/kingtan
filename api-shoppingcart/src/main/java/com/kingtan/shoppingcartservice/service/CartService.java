package com.kingtan.shoppingcartservice.service;

import com.kingtan.shoppingcartservice.model.CartItem;
import com.kingtan.shoppingcartservice.repository.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    public List<CartItem> getAllItems() {
        return cartRepository.findAll();
    }

    public CartItem addItem(CartItem item) {
        return cartRepository.save(item);
    }

    public Optional<CartItem> getItem(Long id) {
        return cartRepository.findById(id);
    }

    public CartItem updateItem(Long id, CartItem updatedItem) {
        return cartRepository.findById(id)
                .map(item -> {
                    item.setProductId(updatedItem.getProductId());
                    item.setProductName(updatedItem.getProductName());
                    item.setQuantity(updatedItem.getQuantity());
                    item.setPrice(updatedItem.getPrice());
                    return cartRepository.save(item);
                })
                .orElseThrow(() -> new RuntimeException("Item not found"));
    }

    public void deleteItem(Long id) {
        cartRepository.deleteById(id);
    }
}
