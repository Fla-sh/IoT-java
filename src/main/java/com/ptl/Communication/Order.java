package com.ptl.Communication;

import com.ptl.Light.Programs.ProgramModel;
import com.ptl.Light.Trigers.TriggerModel;

import java.util.ArrayList;

/**
 * Class containing order produced by ReadOrder class
 * And used by lights controller
 */

public class Order {
    private ProgramModel program;
    private ArrayList<TriggerModel> triggers;

    public Order(ProgramModel program, ArrayList<TriggerModel> triggers){
        this.program = program;
        this.triggers = triggers;
    }

    public ProgramModel getProgram(){
        return program;
    }

    public ArrayList<TriggerModel> getTriggerss(){
        return triggers;
    }

    public void runProgram(){
        program.run();
    }

    public String toString(){
        String result = "";
        result += program.getName() + " | ";
        for(TriggerModel trigger : triggers) result += trigger.getName() + " ";
        return result;
    }
}
