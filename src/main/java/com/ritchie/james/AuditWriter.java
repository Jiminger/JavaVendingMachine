package com.ritchie.james;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;

public class AuditWriter {

    private File auditFile;

    public AuditWriter(String fileName) {

        try {
            this.auditFile = new File(fileName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void reportState(int state) {
            String s = "";
            if (state == 0) {
                s = "Ready.";
            } else if (state == 1) {
                s = "Accepting Money.";
            } else if (state == 2) {
                s = "Selecting Item.";
            } else if (state == 3) {
                s = "Dispensing Item";
            } else if (state == 4) {
                s = "Dispensing Change";
            } else if (state == 5) {
                s = "Loading Machine";
            } else if (state == 6) {
                s = "Machine Turning Off.";
            }
            writeToAuditFile("Machine State: " + state + " (" + s + ")");
    }

    public void reportFileLoadingAttempt(String fileName){
        writeToAuditFile("Attempting to load vending machine file: " + fileName);
    }

    public void reportWasMachineLoaded(boolean wasLoaded){
        if(wasLoaded){
            writeToAuditFile("Machine loading was successful");
        }else{
            writeToAuditFile("ERROR: Machine was NOT loaded.");
        }
    }


    public void writeToAuditFile(String s){
        try{
            PrintWriter writer = new PrintWriter(new FileWriter(this.auditFile, true));
            writer.println(s);
            writer.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}

