package com.ptl.Light;


import com.ptl.Maintain.Printer;
/*
    Main class for lights operation
    Class provide multithreading support
 */
public class Controller implements Runnable {
    private Boolean isActive;
    private final TriggerChecker triggerChecker = new TriggerChecker();
    public Controller(){
        Printer.print(this, "Lights controller started");
        isActive = false;
        new Thread(this, "LightController").start();
    }


    @Override
    public void run() {
        while(true){
            try {
                updateState();
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Check if state of lights should be changed
     * There is absolutely no point to change lights state if there is no change to commit
     * It preserve lights from blinking
     */
    private void updateState(){
        Printer.print(this, "Updating triggers states");
        if(!isActive && shouldActivate()){
            switchOnLights();
        }
        else if(isActive && !shouldActivate()){
            switchOffLights();
        }
    }

    /**
     * Check if something triggered device state change
     * @return true if any trigger occured
     */
    private Boolean shouldActivate(){
        Printer.print(this, "Checking if lights should be activated");
        if(triggerChecker.checkForDevicesPresence().size() != 0) return true;
        else return false;
    }

    private void switchOnLights(){
        Printer.print(this, "Switching on lights");
        isActive = true;
    }

    private void switchOffLights(){
        Printer.print(this, "Switching off lights");
        isActive = false;
    }
}
