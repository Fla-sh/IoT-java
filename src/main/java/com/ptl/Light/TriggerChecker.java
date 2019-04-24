package com.ptl.Light;

import com.ptl.Maintain.CColors;
import com.ptl.Maintain.Printer;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class TriggerChecker {

    private HashMap<String, String> knownDevices;
    private final String result_file_name = "runningDevices.csv";
    private final String known_device_file_name = "known_devices.txt";

    public TriggerChecker(){
        knownDevices = loadKnownDevicesList();
    }

    /**
     * loads file with device addresses and corresponding names to dictionary and
     * returns it
     */
    private HashMap<String, String> loadKnownDevicesList(){
        Printer.print(this, "Loading know_device file");
        HashMap<String, String> knownDevice = new HashMap<>();
        try {
            FileReader fileReader = new FileReader(known_device_file_name);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line = null;

            while((line = bufferedReader.readLine()) != null){
                String[] words = line.split(",");
                knownDevice.put(words[0], words[1]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return knownDevice;
    }

    /**
     * invoke to check if scan result has sth in common with known_devices, if so
     * returns values, which are the same
     * @return found devices which are known
     */
    public ArrayList<String> checkForDevicesPresence(){
        Printer.print(this, "Checking if any device was found in scan");
        ArrayList<String> foundDevices = new ArrayList<>();
            try {
            FileReader fileReader = new FileReader(result_file_name);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            String address = null;
            while((address = bufferedReader.readLine()) != null){
                /**
                 * Single line in running devices contains
                 * device address, avg rssi
                 * so only first elem is needed
                 */
                address = address.split(",")[0];
                if(knownDevices.containsKey(address)){
                    foundDevices.add(knownDevices.get(address));
                    Printer.print(this, "Device " +
                            CColors.GREEN + address
                            + CColors.RESET + " : "
                            + CColors.GREEN + knownDevices.get(address) + CColors.RESET +
                            " found!");
                }
            }
        } catch (
        FileNotFoundException e) {
            e.printStackTrace();
        } catch (
        IOException e) {
            e.printStackTrace();
        }
            return foundDevices;
    }
}
