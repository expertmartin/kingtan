package com.kingtan.shoppingcartservice.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "cartitem")
@Data // Lombok for getters, setters, toString, etc.
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String productId;
    private String productName;
    private int quantity;
    private double price;
}
