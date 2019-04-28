package com.ptl.Communication;

import com.ptl.Light.Programs.ProgramConstantWhite;
import com.ptl.Light.Programs.ProgramModel;
import com.ptl.Light.Programs.ProgramOff;
import com.ptl.Light.Trigers.TagVicinity;
import com.ptl.Light.Trigers.TriggerModel;
import com.ptl.Maintain.Printer;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Class reads orders from swap files
 *      console_order.swap
 *      http_order.swap
 * If no new orders was found class throw exception
 *      OrderNotFound
 * If new order was found class check what program was mentioned in it
 * and whats triggers was in it, and returns it as Order class object
 */

public class ReadOrder {

    public static Order Read(OrderPlace orderPlace) throws OrderNotFoundException {
        HashMap<String, String[]> order = new HashMap<>();
        String[] fileContent = ReadFile(orderPlace);
        ProgramModel program = chooseProgram(fileContent[0]);
        ArrayList<TriggerModel> triggers = chooseTriggers(fileContent[1].split(" "));

        return new Order(program, triggers);
    }

    /**
     * Read file
     * @param orderPlace choose if order should be checked in console file or http file
     * @return array containing
     *              1 - program name
     *              2 - triggers, as string
     * @throws OrderNotFoundException if no orders was found, helpful to immediately stop class execution
     */
    private static String[] ReadFile(OrderPlace orderPlace) throws OrderNotFoundException {
        String fileContent = null;
        FileReader fileReader;
        BufferedReader bufferedReader;
        String line;
        try {
            fileReader = new FileReader(orderPlace.get());
            bufferedReader = new BufferedReader(fileReader);

            line = bufferedReader.readLine();

            if(line != null){
                fileContent = line;
            }
            else throw new OrderNotFoundException();

            fileReader.close();
            bufferedReader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileContent.split(",");
    }

    /**
     * choose which program was mentioned in file
     * @param programName
     * @return class with proper program
     */
    private static ProgramModel chooseProgram(String programName){
        switch(programName){
            case "off":{
                return new ProgramOff();
            }
            case "constant_white":{
                return new ProgramConstantWhite();
            }
            default:{
                System.out.println("Program: " + programName + " not recognized!");
                System.out.println("Setting program off");
                return new ProgramOff();
            }
        }
    }

    /**
     * choose which triggers were mentioned in file
     * @param triggers
     * @return ArrayList of triggers
     */
    private static ArrayList<TriggerModel> chooseTriggers(String[] triggers){
        ArrayList<TriggerModel> result = new ArrayList<>();
        for(String trigger : triggers){
            switch(trigger){
                case "vicinity":{
                    result.add(new TagVicinity());
                    break;
                }
            }
        }
        return result;
    }
}
