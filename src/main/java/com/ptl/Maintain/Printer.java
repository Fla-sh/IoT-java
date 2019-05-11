package com.ptl.Maintain;

import javax.swing.text.DateFormatter;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Used to maintain logging from modules, because standard output is used as console
 */
public class Printer {

    private static final String mainLogFile = "main.log";

    /**
     * Used to save some text to log
     * @param object is source of message to save
     * @param text is text to save
     */
    public static void print(Object object, String text) {
        String color = getColor(object);
        Date now = new Date();
        String date = new SimpleDateFormat("E yyy-MMM-d  H:m:s.S zzz  ").format(now);
        String textToSave = date + CColors.YELLOW +
                color + object.getClass().getCanonicalName() + " : " +
                CColors.RESET + text +
                "\n";

        writeToFile(textToSave);
    }

    /**
     * gets color depending on module
     *
     * @param object class which color should be estamined
     * @return string as color to use in above command
     */
    private static String getColor(Object object) {
        String[] path = object.getClass().getCanonicalName().split("\\.");
        switch (path[2]) {
            case "Lights": {
                return CColors.CYAN_BOLD;
            }
            case "Scanner": {
                return CColors.RED_BOLD;
            }
            default: {
                return CColors.WHITE;
            }
        }
    }

    /**
     * Used to write text on console
     * @param text text to write
     * @deprecated
     */
    private static void writeOnConsole(String text) {
        System.out.println(text);
    }

    /**
     * Used to save text to file in two copies
     * One contains all previously saved lines,
     * and second only last line, it's made to use with tail -f option
     * @param text
     */
    private static void writeToFile(String text) {
        FileWriter logLast = null;
        FileWriter logContinuous = null;
        BufferedWriter bufferedWriter = null;

        try {
            logLast = new FileWriter(mainLogFile + ".last");
            logContinuous = new FileWriter(mainLogFile, true);

            // Write last line to file
            bufferedWriter = new BufferedWriter(logLast);
            bufferedWriter.write(text);
            bufferedWriter.close();
            logLast.close();

            // Append line to file
            bufferedWriter = new BufferedWriter(logContinuous);
            bufferedWriter.write(text);
            bufferedWriter.close();
            logContinuous.close();

        } catch (IOException e) {
            e.printStackTrace();
            try {
                if (logLast != null) logLast.close();
                if (logContinuous != null) logContinuous.close();
                if (bufferedWriter != null) bufferedWriter.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }


        }
    }
}
