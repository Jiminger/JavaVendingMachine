package com.ritchie.james.View;

import com.ritchie.james.Model.Item;

import java.util.Scanner;

public class UserInterface {

    Scanner scanner = new Scanner(System.in);

    public String getUserInput() {
        return scanner.nextLine();
    }

    public void printWelcomeMessage() {
        System.out.println("""
                ******************************************
                *                                        *
                *  Welcome to the Java Vending machine!  *
                *                                        *
                ******************************************""");
    }

    public void printFileLoadMessage(boolean fileLoaded){
        if(fileLoaded){
            System.out.println("File Loaded Successfully.");
        }else{
            System.out.println("Error Loading File.");
        }
    }

    public void printNotEnoughMoney(){
        System.out.println("Insufficient Funds.");
    }

    public void printInvalidInput(){
        System.out.println("ERROR: INVALID INPUT");
    }

    public void printMoneyAddedMessage(){
        System.out.println("Money Accepted");
    }

    public void printZeroStatePrompt() {
        System.out.println("\nState: Ready\nOptions:\n1: View Menu.\n2: Insert Money.\n3: Load New Item List.\nX: Exit");
    }

    public void printOneStatePrompt(double currentMoney){
        System.out.printf("""
                State: Accepting Money
                Current Money: $ %.2f
                Enter Dollar Amount, 'Select' to Choose an Item, or 'X' to Cancel Transaction:\s""", currentMoney);
    }

    public void printTwoStatePrompt(Item[][] items) {
        System.out.println("\nState: Item Selection");
        printMenu(items);
        System.out.println("\nEnter the ID for the item you would like to purchase (or 'x' to cancel): ");
    }

    public void printThreeStatePrompt(Item item){
        System.out.println("\nState: Dispensing Item\nDispensing : " + item.getName());
    }

    public void printFourStatePrompt(double change){
        System.out.printf("\nState: Dispensing Change\nDispensing Change : $ %.2f", change);
    }

    public void printFiveStatePrompt(){
        System.out.println("\nPlease enter the input file name: ");
    }

    public void printSixStatePrompt(){
        System.out.println("\nMachine Shutting Down...");
    }

    public void printMenu(Item[][] items) {
        System.out.format("%-10s%-30s%-15s%-15s%n", "ID", "Name", "Cost ($)", "Inventory");
        System.out.println("-----------------------------------------------------------------");
        for (int i = 0; i < items.length; i++) {
            for (int j = 0; j < items[0].length; j++) {
                if (items[i][j] == null) return;
                Item item = items[i][j];

                String id = "";
                char letter = (char) ('A' + i);
                id += letter;
                id += j;
                if (item.getStock() > 0) {
                    System.out.format("%-10s%-30s%-15.2f%-15s%n", (id), item.getName(), item.getPrice(), item.getStock());
                }
            }
        }
    }

    public void printError(Exception e) {
        e.printStackTrace();
    }


}