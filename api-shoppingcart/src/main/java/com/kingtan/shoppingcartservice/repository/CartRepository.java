package com.kingtan.shoppingcartservice.repository;

import com.kingtan.shoppingcartservice.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<CartItem, Long> {
}
