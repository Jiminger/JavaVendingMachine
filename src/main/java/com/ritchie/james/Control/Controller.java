package com.ritchie.james.Control;

import com.ritchie.james.Model.Item;
import com.ritchie.james.Model.VendingMachine;
import com.ritchie.james.View.UserInterface;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Controller class used to manipulate the model and send the appropriate data to the view to be displayed.
 */

public class Controller {

    private final UserInterface view;
    private final VendingMachine model;


    /**
     * Constructor for the Controller class.
     *
     * @param view  view to be displayed to the console.
     * @param model the data model.
     */
    public Controller(UserInterface view, VendingMachine model) {
        this.view = view;
        this.model = model;
    }

    /**
     * Helper method to "clean" user input -- set input to uppercase and trim leading and trailing whitespace.
     *
     * @param s string to be cleaned.
     * @return string after being cleaned.
     */
    public String cleanUserStringInput(String s) {
        return s.toUpperCase().trim();
    }

    /**
     * Method used to run the controller.
     */
    public void run() {

        reportMachineTurningOn();
        this.view.printWelcomeMessage();
        this.loadMachine("input.json");
        boolean running = true;
        String input = "";

        while (running) {

            while (this.model.getState() == 0) {
                this.view.printZeroStatePrompt();
                input = cleanUserStringInput(this.view.getUserInput());
                switch (input) {
                    case "1" -> this.view.printMenu(this.model.getItems());
                    case "2" -> this.model.setState(1);
                    case "3" -> this.model.setState(5);
                    case "X" -> this.model.setState(6);
                    default -> this.view.printInvalidInput();
                }
            }

            while (this.model.getState() == 1) {
                this.view.printOneStatePrompt(this.model.getCurrentMoney());
                input = cleanUserStringInput(this.view.getUserInput());
                if (input.equals("SELECT")) {
                    this.model.setState(2);

                } else if (input.equals("X")) {
                    reportCancelledTransaction();

                    if (this.model.getCurrentMoney() > 0) {
                        this.model.setState(4);
                    } else {
                        this.model.setState(0);
                    }

                } else {
                    if (addMoney(input)) {
                        this.view.printMoneyAddedMessage();
                    } else {
                        this.view.printInvalidInput();
                    }
                }
            }

            while (this.model.getState() == 2) {
                this.view.printTwoStatePrompt(this.model.getItems());
                input = cleanUserStringInput(this.view.getUserInput());
                if (input.equals("x")) {
                    reportCancelledTransaction();
                    if (this.model.getCurrentMoney() > 0) {
                        this.model.setState(4);
                    } else {
                        this.model.setState(0);
                    }
                } else if (validItemID(input)) {
                    if (enoughMoneyToBuy(inputToItemID(input))) {
                        this.model.setState(3);
                    } else {
                        this.view.printNotEnoughMoney();
                        this.model.setState(1);
                    }
                } else {
                    this.view.printInvalidInput();
                }
            }

            while (this.model.getState() == 3) {
                this.view.printThreeStatePrompt(this.sellItem(inputToItemID(input)));
                if (this.model.getCurrentMoney() > 0) {
                    this.model.setState(4);
                } else {
                    this.model.setState(0);
                }
            }

            while (this.model.getState() == 4) {
                this.view.printFourStatePrompt(this.dispenseChange());
                this.model.setState(0);
            }

            while (this.model.getState() == 5) {
                this.view.printFiveStatePrompt();
                this.view.printFileLoadMessage(loadMachine(cleanFileName(this.view.getUserInput())));
                this.model.setState(0);
            }

            if (this.model.getState() == 6) {
                this.view.printSixStatePrompt();
                reportMachineTurningOff();
                running = false;
            }
        }
    }

    /**
     * Method used to dispense change.
     *
     * @return the change to be dispensed.
     */
    public double dispenseChange() {
        double change = this.model.getCurrentMoney();
        this.model.setCurrentMoney(0);
        reportChangeDispensed(change);
        return change;
    }

    /**
     * Method used to sell an item after making a valid selection.
     *
     * @param indexes int[2] symbolizing the location of the item in the model indexes[0] = row, indexes[1] = column.
     * @return the Item to sell.
     */
    public Item sellItem(int[] indexes) {
        int row = indexes[0];
        int col = indexes[1];
        this.model.setCurrentMoney(this.model.getCurrentMoney() - this.model.getItems()[row][col].getPrice());
        this.model.getItems()[row][col].setStock(this.model.getItems()[row][col].getStock() - 1);
        reportItemPurchased(this.model.getItems()[row][col]);
        return this.model.getItems()[row][col];
    }

    /**
     * Helper method used to "clean" file names inputed by the user -- trims leading and trailing whitespace
     * from the file name.
     *
     * @param input file name to cleaned.
     * @return the cleaned file name.
     */
    public String cleanFileName(String input) {
        return input.trim();
    }

    /**
     * Method used to determine if the machine currently has enough money in it to satisfy the transaction.
     *
     * @param indexes int[2] symbolizing the location of the item in the model indexes[0] = row, indexes[1] = column.
     * @return boolean value describing whether the machine has enough money in it or not.
     */
    public boolean enoughMoneyToBuy(int[] indexes) {
        return this.model.getCurrentMoney() >= this.model.getItems()[indexes[0]][indexes[1]].getPrice();
    }

    /**
     * Method used to change the user selection input into an ID (Example: "A9" -> [0,9])
     *
     * @param input user input to be converted to an item ID
     * @return int[2] symbolizing the location of the item in the model int[0] = row, int[1] = column.
     * (NULL IF INVALID)
     */
    public int[] inputToItemID(String input) {
        try {
            int[] indexes = new int[2];

            char rowChar = input.charAt(0);
            indexes[0] = rowChar - 'A';
            indexes[1] = Integer.parseInt(input.substring(1));
            return indexes;

        } catch (Exception e) {
            this.view.printInputError();
        }

        return null;
    }

    /**
     * Method used to determine if an ItemID is valid based on format, size of item array in model, and current stock.
     *
     * @param input ID to validate.
     * @return boolean symbolizing whether the ID is valid.
     */
    public boolean validItemID(String input) {

        int[] indexes = inputToItemID(input);
        if (indexes == null) return false;

        int row = indexes[0];
        int col = indexes[1];

        if (row >= 0 && row < this.model.getItems().length && col >= 0 && col <= this.model.getItems()[0].length) {
            if (this.model.getItems()[row][col] != null) {
                return this.model.getItems()[row][col].getStock() > 0;
            }
            return false;
        }

        return false;
    }

    /**
     * Method used to add money to the machine.
     *
     * @param input the amount of money to add.
     * @return boolean symbolizing whether money was successfully added or not.
     */
    public boolean addMoney(String input) {
        try {
            double cash = Double.parseDouble(input);
            if (cash > 0) {
                this.model.setCurrentMoney(this.model.getCurrentMoney() + cash);
                reportMoneyAdded(cash);
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Method used to load the machine with a new JSON file.
     *
     * @param fileName the name of the file to load the machine with.
     * @return boolean symbolizing whether the file was added successfully or not.
     */
    public boolean loadMachine(String fileName) {

        reportFileLoadingAttempt(fileName);


        try (FileReader reader = new FileReader(fileName)) {
            JSONParser parser = new JSONParser();
            JSONObject obj = (JSONObject) parser.parse(reader);

            // Getting rows and columns for set up
            JSONObject config = (JSONObject) obj.get("config");
            int rows = (int) (long) config.get("rows");
            int columns = Integer.parseInt((String) config.get("columns"));

            if (rows <= 26 && rows > 0 && columns > 0) {
                Item[][] items = new Item[rows][columns];

                //Stack used to help load the vending machine
                Queue<Item> stackOfItems = new LinkedList<>();

                // Creating Item objects and pushing them on stack used to add to the vending machine.
                JSONArray itemArray = (JSONArray) obj.get("items");
                for (Object value : itemArray) {
                    JSONObject o = (JSONObject) value;
                    String name = (String) o.get("name");
                    double price = Double.parseDouble(((String) o.get("price")).substring(1));
                    int stock = (int) (long) o.get("amount");

                    Item item = new Item(name, price, stock);
                    stackOfItems.add(item);
                }

                // Emptying stack in to the vending machine
                for (int i = 0; i < items.length; i++) {
                    for (int j = 0; j < items[i].length; j++) {
                        if (!stackOfItems.isEmpty()) {
                            items[i][j] = stackOfItems.remove();
                        }
                    }
                }
                this.model.setItems(items);
                // If dimensions too large
            } else {
                System.out.println("here");
                reportWasMachineLoaded(false);
                return false;
            }

        } catch (Exception e) {
            reportWasMachineLoaded(false);
            return false;
        }
        reportWasMachineLoaded(true);
        return true;
    }

    // Auditing Methods

    /**
     * Method used to write messages to the audit file.
     *
     * @param s string to be written to the audit file.
     */
    private void writeToAuditFile(String s) {
        try {
            PrintWriter writer = new PrintWriter(new FileWriter("auditFile.txt", true));
            writer.println(s);
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Method used to report to the audit file that the machine was turned on.
     */
    public void reportMachineTurningOn() {
        writeToAuditFile("Machine Turning On...");
    }

    /**
     * Method used to report to the audit file that the machine is turning off.
     */
    public void reportMachineTurningOff() {
        writeToAuditFile("Machine Turning Off...");
    }

    /**
     * Method used to report to the audit file that money was added to the machine.
     *
     * @param amountAdded the amount of money added to the machine.
     */
    public void reportMoneyAdded(Double amountAdded) {
        writeToAuditFile("Money Added to the Machine: $ %.2f".formatted(amountAdded));
    }

    /**
     * Method used to report to the audit file that an item was purchased.
     *
     * @param item the item that was purchased.
     */
    public void reportItemPurchased(Item item) {
        writeToAuditFile(item.getName() + " Purchased for: $ %.2f  (Remaining Stock: ".formatted(item.getPrice())
                + item.getStock() + ")");
    }

    /**
     * Method used to report to the audit file that change was dispensed.
     *
     * @param change the amount of change dispensed.
     */
    public void reportChangeDispensed(double change) {
        writeToAuditFile("Dispensed Change: $ %.2f".formatted(change));
    }

    /**
     * Method used to report to the audit file that a transaction was cancelled.
     */
    public void reportCancelledTransaction() {
        writeToAuditFile("Transaction Cancelled.");
    }

    /**
     * Method used to report to the audit file whether the machine was load successfully or unsuccessfully.
     *
     * @param wasLoaded boolean symbolizing whether the machine was successfully loaded or not.
     */
    public void reportWasMachineLoaded(boolean wasLoaded) {
        if (wasLoaded) {
            writeToAuditFile("Machine Loading Was Successful");
        } else {
            writeToAuditFile("ERROR: Machine Was NOT Loaded.");
        }
    }

    /**
     * Method used to report to the audit file that the machine is attempting to load from an input file.
     *
     * @param fileName the name of the input file the machine is trying to load with.
     */
    public void reportFileLoadingAttempt(String fileName) {
        writeToAuditFile("Attempting to Load Vending Machine File: " + fileName);
    }

}
