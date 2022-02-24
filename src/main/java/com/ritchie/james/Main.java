package com.ritchie.james;

import com.ritchie.james.Model.VendingMachine;
import com.ritchie.james.View.UserInterface;

/**
 * <p>
 * Program Description:
 * The program is a command line java implementation of a vending machine. The program is structured with an MVC design.
 * The user may run the program make item selections, enter an amount representing money in US dollars, and recieve
 * change back from the transaction. The program comes with a JSON file used for the initial loading of the vending
 * machine but allows the user to later load the machine with a different input file in JSON format. All major actions
 * are also logged to an audit file.
 * </p><p>
 * Requirements to run the program via command line:
 * Java 17.0.2
 * Maven 3.8.4
 * </p>
 *
 * @author James Ritchie
 * @version 1.0
 * @since 2022-02-20
 *
 */

public class Main {
    public static void main(String[] args) {
        Controller controller = new Controller(new UserInterface(), new VendingMachine());
        controller.run();
    }
}