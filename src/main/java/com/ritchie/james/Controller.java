package com.ritchie.james;

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


public class Controller {

    private final UserInterface view;
    private final VendingMachine model;


    public Controller(UserInterface view, VendingMachine model) {
        this.view = view;
        this.model = model;
    }

    public String cleanUserStringInput(String s) {
        return s.toUpperCase().trim();
    }

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

            while (this.model.getState() == 1){
                this.view.printOneStatePrompt(this.model.getCurrentMoney());
                input = cleanUserStringInput(this.view.getUserInput());
                if(input.equals("SELECT")){
                    this.model.setState(2);

                }else if(input.equals("X")){
                    reportCancelledTransaction();

                    if(this.model.getCurrentMoney() > 0){
                        this.model.setState(4);
                    }else{
                        this.model.setState(0);
                    }

                }else{
                    if(addMoney(input)){
                        this.view.printMoneyAddedMessage();
                    }else{
                        this.view.printInvalidInput();
                    }
                }
            }

            while(this.model.getState() == 2){
                this.view.printTwoStatePrompt(this.model.getItems());
                input = cleanUserStringInput(this.view.getUserInput());
                if(input.equals("x")){
                    reportCancelledTransaction();
                    if(this.model.getCurrentMoney() > 0){
                        this.model.setState(4);
                    }else{
                        this.model.setState(0);
                    }
                }else if(validItemID(input)){
                    if(enoughMoneyToBuy(inputToItemID(input))){
                        this.model.setState(3);
                    }else{
                        this.view.printNotEnoughMoney();
                        this.model.setState(1);
                    }
                }else{
                    this.view.printInvalidInput();
                }
            }

            while(this.model.getState() == 3){
                this.view.printThreeStatePrompt(this.sellItem(inputToItemID(input)));
                if(this.model.getCurrentMoney() > 0){
                    this.model.setState(4);
                }else{
                    this.model.setState(0);
                }
            }

            while(this.model.getState() == 4){
                this.view.printFourStatePrompt(this.dispenseChange());
                this.model.setState(0);
            }

            while(this.model.getState() == 5){
                this.view.printFiveStatePrompt();
                this.view.printFileLoadMessage(loadMachine(cleanFileName(this.view.getUserInput())));
                this.model.setState(0);
            }

            if(this.model.getState() == 6){
                this.view.printSixStatePrompt();
                reportMachineTurningOff();
                running = false;
            }
        }
    }


    public double dispenseChange(){
        double change = this.model.getCurrentMoney();
        this.model.setCurrentMoney(0);
        reportChangeDispensed(change);
        return change;
    }

    public Item sellItem(int[] indexes){
        int row = indexes[0];
        int col = indexes[1];
        this.model.setCurrentMoney(this.model.getCurrentMoney() - this.model.getItems()[row][col].getPrice());
        this.model.getItems()[row][col].setStock(this.model.getItems()[row][col].getStock() - 1);
        reportItemPurchased(this.model.getItems()[row][col]);
        return this.model.getItems()[row][col];
    }

    public String cleanFileName(String input){
        return input.trim();
    }

    public boolean enoughMoneyToBuy(int[] indexes){
        return this.model.getCurrentMoney() > this.model.getItems()[indexes[0]][indexes[1]].getPrice();
    }

    public int[] inputToItemID(String input){
        try{
            int[] indexes = new int[2];

            char rowChar = input.charAt(0);
            indexes[0] = rowChar - 'A';
            indexes[1] = Integer.parseInt(input.substring(1));
            return indexes;

        }catch(Exception e){
            this.view.printError(e);
        }

        return null;
    }

    public boolean validItemID(String input){

        int[] indexes = inputToItemID(input);
        if(indexes == null) return false;

        int row = indexes[0];
        int col = indexes[1];

        if(row >= 0 && row < this.model.getItems().length && col >= 0 && col <= this.model.getItems()[0].length){
            if(this.model.getItems()[row][col] != null){
                return this.model.getItems()[row][col].getStock() > 0;
            }
            return false;
        }

        return false;
    }

    public boolean addMoney(String input){
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

    public boolean loadMachine(String fileName) {

        reportFileLoadingAttempt(fileName);


        try (FileReader reader = new FileReader(fileName)) {
            JSONParser parser = new JSONParser();
            JSONObject obj = (JSONObject) parser.parse(reader);

            // Getting rows and columns for set up
            JSONObject config = (JSONObject) obj.get("config");
            int rows = (int) (long) config.get("rows");
            int columns = Integer.parseInt((String) config.get("columns"));

            if (rows < 26 && rows > 0 && columns > 0) {
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
                reportWasMachineLoaded(false);
                return false;
            }

        } catch (Exception e) {
            this.view.printError(e);
            reportWasMachineLoaded(false);
            return false;
        }
        reportWasMachineLoaded(true);
        return true;
    }


    // Auditing Methods
    private void writeToAuditFile(String s){
        try{
            PrintWriter writer = new PrintWriter(new FileWriter("auditFile.txt", true));
            writer.println(s);
            writer.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void reportMachineTurningOn(){
        writeToAuditFile("Machine Turning On...");
    }

    public void reportMachineTurningOff() { writeToAuditFile("Machine Turning Off...");}

    public void reportMoneyAdded(Double amountAdded){
        writeToAuditFile("Money Added to the Machine: $ %.2f".formatted(amountAdded));
    }

    public void reportItemPurchased(Item item){
        writeToAuditFile(item.getName() + " Purchased for: $ %.2f  (Remaining Stock: ".formatted(item.getPrice())
                + item.getStock() + ")");
    }

    public void reportChangeDispensed(double change){
        writeToAuditFile("Dispensed Change: %.2f".formatted(change));
    }

    public void reportCancelledTransaction(){
        writeToAuditFile("Transaction Cancelled.");
    }

    public void reportWasMachineLoaded(boolean wasLoaded){
        if(wasLoaded){
            writeToAuditFile("Machine Loading Was Successful");
        }else{
            writeToAuditFile("ERROR: Machine Was NOT Loaded.");
        }
    }

    public void reportFileLoadingAttempt(String fileName){
        writeToAuditFile("Attempting to Load Vending Machine File: " + fileName);
    }


}