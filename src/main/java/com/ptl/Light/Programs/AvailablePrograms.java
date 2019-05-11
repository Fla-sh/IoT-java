package com.ptl.Light.Programs;

/**
 * Describe available lights programs
 *
 * @see com.ptl.Light.Controller
 * @see com.ptl.Communication.Console.Parsers.LightParser
 */
public enum AvailablePrograms {
    CONSTANT_WHITE("constant_white"),
    OFF("off");

    private String name;
    AvailablePrograms(String name){
        this.name = name;
    }

    public String getName(){
        return name;
    }
}
