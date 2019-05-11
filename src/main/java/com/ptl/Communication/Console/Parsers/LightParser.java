package com.ptl.Communication.Console.Parsers;

import com.ptl.Communication.Console.ParsingError;
import com.ptl.Communication.Messages.MessageSource;
import com.ptl.Light.Programs.AvailablePrograms;
import com.ptl.Light.Trigers.AvailableTriggers;
import com.ptl.Maintain.EnumUtil;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Parse information form console into file, which later on will be read
 * by ReadLightMessage class
 *
 * @see com.ptl.Communication.Messages.LightMessage.ReadLightMessage
 */
public class LightParser extends ParserModel {
    /**
     * Main function of this parser
     * Takes line from console and save light program and triggers read from this line
     * to /src/java/com/ptl/Communication/Messages/LightMessage/message.swap
     *
     * Parsed commands are:
     * lights set constant_white tag_vicinity
     * lights help
     *
     * @param command text to parse
     * @see com.ptl.Communication.Console.ConsoleParser
     */
    @Override
    public void parse(String[] command) throws ParsingError, ArrayIndexOutOfBoundsException {
        /*
        How it works:
        - check if user want help
        - check if user want to set sth
        - check if program supplied by user is in AvailablePrograms
        - check if triggers are in AvailableTriggers
        - if eth is correct save it to file

        During all process is possibility to throw an ArrayIndexOutOfBound
        in spite of missing some arguments
         */
        String textToSave = "";
        if (command[1].equals("help")) getHelp();
        else if (command[1].equals("set")) {
            if (EnumUtil.enumContains(AvailablePrograms.class, command[2].toUpperCase())) {
                textToSave += command[2] + ",";
                if(command.length > 2) {
                    for (int i = 3; i < command.length; i++) {
                        if (!EnumUtil.enumContains(AvailableTriggers.class, command[i].toUpperCase())) {
                            throw new ParsingError();
                        } else {
                            textToSave += command[i];
                        }
                    }
                    saveToFile(textToSave);
                }
            } else throw new ParsingError();
        } else throw new ParsingError();
    }

    /**
     * Saves data from text to swap file, which will be read by ReadLightMessage
     *
     * @see com.ptl.Communication.Messages.LightMessage.ReadLightMessage
     * @param text String to save
     */
    private void saveToFile(String text) {
        FileWriter fileWriter = null;
        BufferedWriter bufferedWriter = null;
        try {
            fileWriter = new FileWriter(MessageSource.LIGHT.get());
            bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(text);
            bufferedWriter.close();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    /**
     * Name of parser
     */
    public String getName() {
        return "Light parser";
    }

    @Override
    /**
     * Function printing help for command
     */
    public void getHelp() {
        String response = getName() + "\n";
        response += "Correct input is:" +
                "   program_name trigger trigger2 ... \n\n" +
                "   available programs are: \n";

        for (AvailablePrograms program : AvailablePrograms.values()) {
            response += "       - " + program.getName() + "\n";
        }

        response += "\n";
        response += "  avaliable triggers are: \n";

        for (AvailableTriggers trigger : AvailableTriggers.values()) {
            response += "       - " + trigger.getName() + "\n";
        }

        System.out.println(response);
    }


}
