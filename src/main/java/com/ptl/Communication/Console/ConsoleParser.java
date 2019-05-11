package com.ptl.Communication.Console;

import com.ptl.Communication.Console.Parsers.LightParser;
import com.ptl.Communication.Console.Parsers.ParserModel;

import java.io.Console;
import java.util.Scanner;

/**
 * Is a one of modules used to parse command line input
 * Main purpose is to read input stream from console, parse it
 * and write commands to proper file in Messages module
 */
public class ConsoleParser implements Runnable{

    public ConsoleParser(){
        System.out.println("Console Parser started");
        new Thread(this).start();
    }

    /**
     * main loop of console module
     * line with command contains: module_name command_of_module
     */
    @Override
    public void run() {
        while(true){
            /*
            Module works as separate thread so it can wait until
            someone write sth to console, and then parse it
             */
            String[] command = new Scanner(System.in).nextLine().split(" ");
            ParserModel parser = chooseParser(command);
            /*
            Program must catch cases when user just tap enter, without writing
            something, or use module name that is not know, then we just omit it
             */
            if(parser == null) continue;
            try {
                /*
                Parser could throw Parsing error which mean line form command line
                is either malformed or has insufficient data or length
                 */
                parser.parse(command);
            } catch (ParsingError parsingError) {
                /*
                If such case occurs then we have to write help, and wait for another command
                 */
                parsingError.printStackTrace();
                parser.getHelp();
            }
            catch (ArrayIndexOutOfBoundsException error){
                /*
                another case could occur, when user types correct module name,
                but provide only it, and nth more, then ArrayIndexOutOfBoundsException
                is thrown
                As previously we print help and wait
                 */
                System.out.println("Insufficient parameters count");
                error.printStackTrace();
                getHelp();
            }
        }
    }

    /**
     * Function used to make decision which parser should be used for parsing this command
     * Name of parser to use is first parameter in command line
     * @param command command from which we like to determine parer
     * @return
     */
    private ParserModel chooseParser(String[] command){
        switch (command[0]){
            case "lights":{
                return new LightParser();
            }
            default:{
                getHelp();
                return null;
            }
        }
    }

    /**
     * Prints help if user makes some errors
     */
    private void getHelp(){
        System.out.println("IoT console Welcome! \n" +
                "You can use one of available modules: \n" +
                "   - lights \n\n" +
                "You can also write module name appended with help, to read module manual");
    }

}
