package com.example.assigment3.Model;

public class Product {

    private int id;
    private String name;
    private int price;
    private int quantity;

    public Product(){

    }

    public Product(int id, String name, int price, int stock) {
        this.id = id;
        this.price=price;
        this.name = name;
        this.quantity = stock;
    }

    public Product(String name, int price, int stock) {
        this.price=price;
        this.name = name;
        this.quantity = stock;
    }

    public void setPrice(int price){this.price=price;}

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getPrice() {return price;}

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price='" + price + '\'' +
                ", quantity=" + quantity +
                '}';
    }
}
