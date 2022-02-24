package com.ritchie.james.View;

import com.ritchie.james.Model.Item;

import java.util.Scanner;

/**
 * UserInterface class used to retrieve user input and format output to the console.
 */
public class UserInterface {

    Scanner scanner = new Scanner(System.in);

    /**
     * Method used to retrieve user input
     * @return next line from the console
     */
    public String getUserInput() {
        return scanner.nextLine();
    }

    /**
     * Method used to print welcome message to the console.
     */
    public void printWelcomeMessage() {
        System.out.println("""
                ******************************************
                *                                        *
                *  Welcome to the Java Vending machine!  *
                *                                        *
                ******************************************""");
    }

    /**
     * Method used to output a message based on whether the input file was loaded successfully.
     * @param fileLoaded boolean value representing whether file was loaded.
     */
    public void printFileLoadMessage(boolean fileLoaded){
        if(fileLoaded){
            System.out.println("File Loaded Successfully.");
        }else{
            System.out.println("Error Loading File.");
        }
    }

    /**
     * Method used to print a message indicating there are insufficient funds for an item.
     */
    public void printNotEnoughMoney(){
        System.out.println("Insufficient Funds.");
    }

    /**
     * Method used to print out an error message indicating there was invalid input by the user.
     */
    public void printInvalidInput(){
        System.out.println("ERROR: INVALID INPUT");
    }

    /**
     * Method used to print a message out indicating that money was added successfully.
     */
    public void printMoneyAddedMessage(){
        System.out.println("Money Accepted");
    }

    /**
     * Method used to print out a prompt for when the machine is in state zero (Ready).
     */
    public void printZeroStatePrompt() {
        System.out.println("\nState: Ready\nOptions:\n1: View Menu.\n2: Insert Money.\n3: Load New Item List.\nX: Exit");
    }

    /**
     * Method used to print out a prompt for when the machine is in state one (Accepting Money).
     * @param currentMoney the current amount of money in the machine.
     */
    public void printOneStatePrompt(double currentMoney){
        System.out.printf("""
                \nState: Accepting Money
                Current Money: $ %.2f
                Enter Dollar Amount, 'Select' to Choose an Item, or 'X' to Cancel Transaction:\s""", currentMoney);
    }

    /**
     * Method used to print out a prompt for when the machine is in state two (Item Selection).
     * @param items the array of items to be displayed to the user
     */
    public void printTwoStatePrompt(Item[][] items) {
        System.out.println("\nState: Item Selection");
        printMenu(items);
        System.out.println("\nEnter the ID for the item you would like to purchase (or 'x' to cancel): ");
    }

    /**
     * Method used to print out a prompt for when the machine is in state three (Dispensing Item).
     * @param item the item being dispensed.
     */
    public void printThreeStatePrompt(Item item){
        System.out.println("\nState: Dispensing Item\nDispensing : " + item.getName());
    }

    /**
     * Method used to print out a prompt for when the machine is in state four (Dispensing Change).
     * @param change the amount of change being dispensed.
     */
    public void printFourStatePrompt(double change){
        System.out.printf("\nState: Dispensing Change\nDispensing Change : $ %.2f\n", change);
    }

    /**
     * Method used to print out a prompt for when the machine is in state five (Loading Machine).
     */
    public void printFiveStatePrompt(){
        System.out.println("\nPlease enter the input file name: ");
    }

    /**
     * Method used to print out a prompt for when the machine is in state six (Shutting Down).
     */
    public void printSixStatePrompt(){
        System.out.println("\nMachine Shutting Down...");
    }

    /**
     * Method used to print out the menu of available items
     * @param items the array of items to print out.
     */
    public void printMenu(Item[][] items) {
        System.out.format("\n%-10s%-30s%-15s%-15s%n", "ID", "Name", "Cost ($)", "Inventory");
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

    /**
     * Method used to print out error message when there is invalid input.
     */
    public void printInputError() {
        System.out.println("ERROR INVALID INPUT");;
    }

}