package com.ptl.Maintain;

public class Printer {
    /**
     * decorator for normal print command, adds class name and proper for each module
     * color of class name
     * @param object class from which comes print call
     * @param text text to print
     */
    public static void print(Object object, String text){
        String color = getColor(object);
        System.out.println(color +
                object.getClass().getCanonicalName() + " : " +
                CColors.RESET + text);
    }

    /**
     * gets color depending on module
     * @param object class which color should be estamined
     * @return string as color to use in above command
     */
    private static String getColor(Object object){
        String[] path = object.getClass().getCanonicalName().split("\\.");
        switch(path[2]){
            case "Lights":{
                return CColors.CYAN_BOLD;
            }
            case "Scanner":{
                return CColors.RED_BOLD;
            }
            default:{
                return CColors.WHITE;
            }
        }
    }
}
