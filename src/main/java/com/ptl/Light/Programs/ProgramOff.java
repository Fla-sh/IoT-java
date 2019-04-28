package com.ptl.Light.Programs;

/**
 * Program to permanently switch off lights
 */
public class ProgramOff extends ProgramModel{

    private final String name = "Off";

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void run() {

    }
    //TODO switch off all lights
}
