package com.ritchie.james;

import com.ritchie.james.Controller.Controller;
import com.ritchie.james.Model.VendingMachine;
import com.ritchie.james.View.UserInterface;

public class Main {

    public static void main (String[] args){
        Controller controller = new Controller(new UserInterface(), new VendingMachine());
        controller.run();
    }
}