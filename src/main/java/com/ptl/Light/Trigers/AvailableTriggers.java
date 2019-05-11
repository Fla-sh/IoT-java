package com.ptl.Light.Trigers;

/**
 * Describe available triggers
 *
 * @see com.ptl.Communication.Console.Parsers.LightParser
 * @see com.ptl.Light.Controller
 */
public enum AvailableTriggers {
    TAG_VICINITY("tag_vicinity");

    private String trigger;

    AvailableTriggers(String trigger){
        this.trigger = trigger;
    }

    /**
     * Return chosen element as string
     * @return
     */
    public String getName(){
        return trigger;
    }
}
