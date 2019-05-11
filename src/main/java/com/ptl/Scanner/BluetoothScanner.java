package com.ptl.Scanner;

import com.ptl.Maintain.Printer;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;

/**
 * to search for Bluetooth devices in vicinity
 *     results are saved to result.csv file in format
 *     device address,avg rssi
 *     to file is appended timestamp as last line
 */
public class BluetoothScanner implements Runnable {
    private final String[] gain_sudo_command = {"/bin/bash", "-c", "echo \"instrukcja\" | sudo -S apt-getName"};
    private final String[] scan_command = {"/bin/bash", "-c", "sudo /usr/bin/btmgmt find"};
    private final String result_file_name = "runningDevices.csv";
    private HashMap<String, ArrayList<Integer>> found_devices;
    private ArrayList<String> scan_result;

    public BluetoothScanner(){
        Printer.print(this, "Bluetooth scanner started");
        found_devices = new HashMap<>();
        scan_result = new ArrayList<>();
        new Thread(this, "ScannerThread").start();
    }

    public void run() {
        while(true) {
            this.gainSudo();
            this.scan();
            this.analyseScan();
            this.evaluateAvarnge();
            this.saveResult();
        }
    }

    /**
     * gains sudo privilege
     */
    private void gainSudo(){
        /*
         * running command make us to have sudo permissions, and also
         * cause problems if called simultaneously with btmgmt command
         * so sudo must be firstly used on some other command
         */
        Printer.print(this, "Gaining sudo for scan cast");
        ProcessBuilder processBuilder = new ProcessBuilder().command(gain_sudo_command);
        try {
            Process process = processBuilder.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * function append to known_devices map values of rssi for given device, or create
     * new element in hash map if no previous rssi was found
     * @param rssi current device rssi
     * @param addres current device address
     */
    private void registerDevice(int rssi, String addres){
        Printer.print(this, "Appending to device: " +
                addres + " value: " +
                rssi);
        if(found_devices.containsKey(addres)){
            found_devices.get(addres).add(rssi);
        }
        else{
            ArrayList<Integer> list = new ArrayList<>();
            list.add(rssi);
            found_devices.put(addres, list);
        }
    }

    /**
     * Function start linux command btmgmt
     * and save it result as ArrayList to variable
     */
    private void scan(){
        /*
        Each call of this command must starts with reset of found devices hash map
        or previously discovered devices will be shown again
         */
        found_devices = new HashMap<>();

        Printer.print(this, "Invoking btmgmt command");
        ArrayList<String> response = new ArrayList<>();
        try {
            ProcessBuilder processBuilder = new ProcessBuilder();
            processBuilder.command(scan_command);
            Process process = processBuilder.start();
            InputStreamReader inputStreamReader = new InputStreamReader(process.getInputStream());
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = null;

            while((line = reader.readLine()) != null){
                response.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        scan_result = response;
    }

    /**
     Function analyse results of scan iterating over scan_result
     and searching for pairs of device name and rssi.
     If such pair was found function registerDevice is called
     */
    private void analyseScan(){
        /*
        Device name is always after dev_found:
        Rssi is always after rssi:
         */
        Printer.print(this, "Analysing scan result");
        for (String line: scan_result) {
            String words[] = line.split(" ");
            Integer rssi = 0;
            String address = null;
            for(int i = 0; i < words.length; i++){
                if(words[i].equals("dev_found:")){
                    address = words[i + 1];
                }
                else if(words[i].equals("rssi")){
                    rssi = Integer.valueOf(words[i + 1]);
                }
            }
            if(rssi != 0 && address != null) registerDevice(rssi, address);
        }
    }

    /**
     * Function used to evaluate average of values of received rssi for each device
     */
    private void evaluateAvarnge(){
        /*
        found_devices HashMap so far contains String as key, and ArrayList as values
        ArrayList contains sequence of Integers corresponding to rssi values
        Now we need to transform these rssi values to average, but whole process needs to
        end up saving one average value as ArrayList found_devices HashMap
         */
        Printer.print(this, "Evaluating averages for devices in scan result");
        for(String key: found_devices.keySet()){
            Integer sum = 0;
            for(Integer value: found_devices.get(key)) sum += value;
            Integer avg = sum / found_devices.get(key).size();
            ArrayList<Integer> list = new ArrayList<>();
            list.add(avg);
            found_devices.put(key, list);
        }
    }

    /**
     * Function takes global variable found_device and save it to file
     * result.csv in format given in class description
     */
    private void saveResult(){
        Printer.print(this, "Saving scan result to csv file");
        FileWriter fileWriter = null;
        BufferedWriter bufferedWriter = null;
        try {
            fileWriter = new FileWriter(result_file_name, false);
            bufferedWriter = new BufferedWriter(fileWriter);

            for(String key: found_devices.keySet()){
                String line = key + "," + found_devices.get(key).get(0) + "\n";
                //System.out.println(line);
                bufferedWriter.write(line);

            }
            bufferedWriter.write(new SimpleDateFormat("E dd-MM-yyyy   HH:mm:ss").format(new Date()));
            bufferedWriter.close();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {

            try {
                bufferedWriter.close();
                fileWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}

