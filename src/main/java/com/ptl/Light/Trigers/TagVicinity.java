package com.ptl.Light.Trigers;

import com.ptl.Maintain.CColors;
import com.ptl.Maintain.Printer;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * This trigger check if any of known devices are in vicinity
 * Firstly it reads file with known devices
 * Compares it with scan results
 * Returns true if both file contains at least one common line and
 * signal was strength enough
 */

public class TagVicinity extends TriggerModel{
    private HashMap<String, String> knownDevices;
    private final String name = "Tag Vicinity";
    private final String result_file_name = "runningDevices.csv";
    private final String known_device_file_name = "known_devices.txt";

    public TagVicinity(){
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
                /**
                 * First word contains address of device and second its name
                 */
                String[] words = line.split(",");
                knownDevice.put(words[0], words[1]);
            }
            fileReader.close();
            bufferedReader.close();
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
    private ArrayList<String> checkForDevicesPresence(){
        Printer.print(this, "Checking if any device was found in scan");
        ArrayList<String> foundDevices = new ArrayList<>();
        try {
            FileReader fileReader = new FileReader(result_file_name);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            String lineOfScanResult;
            while((lineOfScanResult = bufferedReader.readLine()) != null){
                /**
                 * Single line in running devices contains
                 * device lineOfScanResult, avg rssi
                 * so only first elem is needed
                 */
                String[] words = lineOfScanResult.split(",");
                /**
                 * Last line of runningDevices.csv contains timestamp, which
                 * is converted to one element array, it could be omitted
                 * because it don't contains valuable informations
                 */
                if(words.length > 1) {
                    String address = words[0];
                    String rssi = words[1].strip();

                    /**
                     * Device is marked as found only if some threshold was overcome
                     * If so device is added to list with spotted devices
                     * and proper communicate is written
                     */

                    Printer.print(this, "Device " +
                            CColors.GREEN + address
                            + CColors.RESET + " : "
                            + CColors.GREEN + knownDevices.get(address) + CColors.RESET + ":" +
                            CColors.GREEN + rssi + CColors.RESET +
                            " found!");

                    if (Integer.parseInt(rssi) > -80) {
                        if (knownDevices.containsKey(address)) {
                            foundDevices.add(knownDevices.get(address));
                            Printer.print(this, "Added as spotted device");
                        }
                    }
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

    /**
     * funciton to use in controller
     * @return true if any of known devices was found
     */
    @Override
    public Boolean check() {
        if(checkForDevicesPresence().size() != 0) return true;
        else return false;
    }

    @Override
    public String getName() {
        return name;
    }
}
