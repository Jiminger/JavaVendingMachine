package com.ritchie.james;

public class VendingMachine {

    private Item[][] items;
    private double currentMoney;
    private int state;

    public VendingMachine() {
        this.items = new Item[0][0];
        this.currentMoney = 0;
        this.state = 0;
    }

    public Item[][] getItems() {
        return items;
    }

    public double getCurrentMoney() {
        return currentMoney;
    }

    public int getState() {
        return state;
    }

    public void setItems(Item[][] items) {
        this.items = items;
    }

    public void setState(int state) {
        this.state = state;
    }

    public double addMoney(double moneyToAdd) {
        this.currentMoney += moneyToAdd;
        return this.currentMoney;
    }

    public double dispenseChange() {
        double change = this.currentMoney;
        this.currentMoney = 0;
        return change;
    }

    public Item sellItem(int[] indexes) {
        int row = indexes[0];
        int col = indexes[1];
        this.currentMoney -= this.items[row][col].getPrice();
        this.items[row][col].setStock(this.items[row][col].getStock() - 1);
        return this.items[row][col];
    }


}