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

    public void setCurrentMoney(double currentMoney) {
        this.currentMoney = currentMoney;
    }


}