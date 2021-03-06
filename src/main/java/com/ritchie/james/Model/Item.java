package com.ritchie.james.Model;

/**
 * Item class storing information about the items in the vending machine.
 */
public class Item {

    private final String name;
    private final Double price;
    private int stock;

    public Item(String name, Double price, int stock) {
        this.name = name;
        this.price = price;
        this.stock = stock;
    }

    public String getName() {
        return name;
    }

    public Double getPrice() {
        return price;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

}