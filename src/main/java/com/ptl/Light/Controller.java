package com.ptl.Light;


import com.ptl.Communication.Messages.LightMessage.LightMessage;
import com.ptl.Communication.Messages.LightMessage.ReadLightMessage;
import com.ptl.Communication.Messages.MessageNotFoundException;
import com.ptl.Light.Programs.ProgramOff;
import com.ptl.Light.Trigers.TriggerModel;
import com.ptl.Maintain.CColors;
import com.ptl.Maintain.Printer;

import java.util.ArrayList;

/**
 * Class is main module of light control system
 * For each second class check if some triggers was fired
 *
 * if new order come and it is triggered run new program
 * this case is special and needed because if trigger for previous order was fired
 * and trigger for new order will be fired too, so system treat it as there was no change at all
 * Surely it's mistake, so that's the point for special if clause
 * Case in which for previous order trigger was fired and now is not is covered in 4th if clause
 *
 * if previously trigger was not fired but now is it's high time to execute program
 *
 * if previously trigger was fired and now is also fired just idle
 *
 * if previously trigger was fired but now it is not it's time to switch off lights
 */
public class Controller implements Runnable {
    private Boolean newOrder;
    private Boolean wasTriggered;
    private LightMessage actualMessage;

    public Controller(){
        System.out.println("Lights controller started");
        newOrder = false;
        wasTriggered = false;
        actualMessage = new LightMessage(new ProgramOff(), new ArrayList<>());
        new Thread(this, "LightController").start();
    }


    @Override
    public void run() {
        Printer.print(this, "\n-------\n");
        while(true){
            try {
                updateState();
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Printer.print(this, "\n-------\n");
        }
    }

    /**
     * Check if state of lights should be changed
     * There is absolutely no point to change lights state if there is no change to commit
     * It preserve lights from blinking
     */
    private void updateState(){
        Printer.print(this, "Updating triggers states");
        checkForNewOrders();
        Boolean trigger = isTriggered();
        if(newOrder && trigger){
            Printer.print(this, "New order come, and was successful triggered");
            Printer.print(this, "Executing new program");
            actualMessage.runProgram();
            newOrder = false;
            wasTriggered = false;
        }
        else if(!wasTriggered && trigger){
            Printer.print(this, "No new order come, but some trigger occurred");
            Printer.print(this, "Executing program");
            actualMessage.runProgram();
            wasTriggered = true;
        }
        else if(!wasTriggered && !trigger){
            Printer.print(this, "No new order come, no trigger occurred");
            Printer.print(this, "Idling");
        }
        else if(wasTriggered && trigger) {
            Printer.print(this, "No new order come, no trigger occurred");
            Printer.print(this, "Idling");
        }
        else if(wasTriggered && !trigger){
            Printer.print(this, "Lights are active, but trigger stopped occurring");
            Printer.print(this, "Switching off lights");
            new ProgramOff();
            wasTriggered = false;
        }
    }

    /**
     * Check if some new orders appear in swap file, if so update actualMessage field
     */
    private void checkForNewOrders(){
        Printer.print(this, "Checking for new orders");
        try {
            actualMessage = ReadLightMessage.read();
            newOrder = true;
            Printer.print(this, "New order found!");
            Printer.print(this, "New order is: \n" +
                    actualMessage.toString());

        } catch (MessageNotFoundException ex) {
            Printer.print(this, "No new orders found");
        }

    }

    /**
     * Check if something triggered device state change
     * @return true if any trigger occured or no trigger was mentioned in order
     */
    private Boolean isTriggered(){
        Printer.print(this, "Checking if triggers was triggered");
        /**
         * triggers contains array with triggers
         */
        ArrayList<TriggerModel> triggers = actualMessage.getTriggerss();
        if(triggers.size() != 0) {
            for (TriggerModel trigger : triggers) {
                if(!trigger.check()){
                    Printer.print(this, CColors.CYAN + trigger.getName() + CColors.RESET + " is not triggered");
                    Printer.print(this, "Breaking trigger look up");
                    return false;
                }
                Printer.print(this, CColors.CYAN + trigger.getName() + CColors.RESET + " is triggered");
            }
            Printer.print(this, "All triggers checked");
        }
        else{
            Printer.print(this, "No triggers found");
        }
        return true;
    }
}
