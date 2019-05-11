package com.ptl.Communication.Messages.LightMessage;

import com.ptl.Light.Programs.ProgramModel;
import com.ptl.Light.Trigers.TriggerModel;

import java.util.ArrayList;

/**
 * Is used as part of communication between user and system.
 * This Message is used to control light module, and contains information
 * from message.swap read by ReadLightMessage.
 * Message.swap is filled by proper command line parser
 *
 * @see ReadLightMessage
 * @see com.ptl.Communication.Console.Parsers.LightParser
 */
public class LightMessage {
    private ProgramModel program;
    private ArrayList<TriggerModel> triggers;

    public LightMessage(ProgramModel program, ArrayList<TriggerModel> triggers){
        this.program = program;
        this.triggers = triggers;
    }

    /**
     * Returns program stored in message
     * @return
     */
    public ProgramModel getProgram(){
        return program;
    }

    /**
     * Returns trigger stored in message
     * @return ArrayList with triggers
     */
    public ArrayList<TriggerModel> getTriggerss(){
        return triggers;
    }

    /**
     * Runs the program stored in message
     */
    public void runProgram(){
        program.run();
    }

    /**
     * Represent message as string
     * @return
     */
    public String toString(){
        String result = "";
        result += program.getName() + " | ";
        for(TriggerModel trigger : triggers) result += trigger.getName() + " ";
        return result;
    }
}
