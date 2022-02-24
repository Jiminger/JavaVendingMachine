package com.ritchie.james.Model;

/**
 * Vending machine class. Stores items in a 2D Array, also keeps track of the current amount of money in the machine
 * as well as the state of the machine.
 * The machine states are as follows:
 * 0 - Ready
 * 1 - Accepting Money
 * 2 - Item Selection
 * 3 - Dispensing Item
 * 4 - Dispensing Change
 * 5 - Loading Machine
 * 6 - Machine Turning Off.
 */

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