package com.ptl.Communication.Messages.LightMessage;

import com.ptl.Communication.Messages.MessageNotFoundException;
import com.ptl.Communication.Messages.MessageSource;
import com.ptl.Light.Programs.AvailablePrograms;
import com.ptl.Light.Programs.ProgramConstantWhite;
import com.ptl.Light.Programs.ProgramModel;
import com.ptl.Light.Programs.ProgramOff;
import com.ptl.Light.Trigers.AvailableTriggers;
import com.ptl.Light.Trigers.TagVicinity;
import com.ptl.Light.Trigers.TriggerModel;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Class reads orders from swap files
 *      Messages/LightMessage/message.swap
 * If no new orders was found class throw exception
 *      MessageNotFoundException
 * If new order was found class check what program was mentioned in it
 * and whats triggers was in it, and returns it as LightMessage class object
 *
 * @see com.ptl.Communication.Console.Parsers.LightParser
 */
public class ReadLightMessage {

    /**
     * Main function in this class, read swap file and return proper message
     * @return LightMessage
     * @throws MessageNotFoundException
     */
    public static LightMessage read() throws MessageNotFoundException {
        String fileContent = readFile();

        ArrayList<TriggerModel> triggers = chooseTriggers(fileContent);
        ProgramModel program = chooseProgram(fileContent);

        return new LightMessage(program, triggers);
    }

    /**
     * Reads file and returns it as string
     * @return String containing program_name,trigger1 trigger2 ...
     * @throws MessageNotFoundException if no orders was found, helpful to immediately stop class execution
     */
    private static String readFile() throws MessageNotFoundException {
        String fileContent = null;
        FileReader fileReader;
        BufferedReader bufferedReader;
        FileWriter fileWriter;
        BufferedWriter bufferedWriter;
        String line;
        try {
            fileReader = new FileReader(MessageSource.LIGHT.get());
            bufferedReader = new BufferedReader(fileReader);

            line = bufferedReader.readLine();

            if(line != null){
                fileContent = line;
            }
            else throw new MessageNotFoundException();

            fileReader.close();
            bufferedReader.close();

            fileWriter = new FileWriter(MessageSource.LIGHT.get());
            bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write("");
            fileWriter.close();
            bufferedWriter.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileContent;
    }

    /**
     * choose which program was mentioned in file
     * @param text text read from swap file
     * @return class with proper program
     */
    private static ProgramModel chooseProgram(String text){
        /*
        program name is element before comma
        program name must be converted to upper case to match enumeration values
         */
        String programName = text.split(",")[0].toUpperCase();
        AvailablePrograms program = AvailablePrograms.valueOf(programName);

        switch(program){
            case OFF:{
                return new ProgramOff();
            }
            case CONSTANT_WHITE:{
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
     * @param text text from swap file
     * @return ArrayList of triggers
     */
    private static ArrayList<TriggerModel> chooseTriggers(String text){
        /*
        triggers are stored after comma and are separated by spaces
         */
        ArrayList<TriggerModel> result = new ArrayList<>();
        String[] triggers = text.split(",")[1].split(" ");

        for(String trigger : triggers){
            AvailableTriggers availableTriggers = AvailableTriggers.valueOf(trigger.toUpperCase());
            switch(availableTriggers){
                case TAG_VICINITY:{
                    result.add(new TagVicinity());
                    break;
                }
            }
        }
        return result;
    }
}
