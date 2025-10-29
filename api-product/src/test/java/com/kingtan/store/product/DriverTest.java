package com.kingtan.store.product;

public class DriverTest {
    public static void main(String[] args) {
        try {
            Class.forName("org.postgresql.Driver");
            System.out.println("PostgreSQL driver found!");
        } catch (ClassNotFoundException e) {
            System.out.println("PostgreSQL driver not found: " + e.getMessage());
        }
    }
}
