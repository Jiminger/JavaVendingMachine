package com.ritchie.james;

public class Main {

    public static void main (String[] args){
        Controller controller = new Controller(new UserInterface(), new VendingMachine());
        controller.run();
    }
}