package com.ptl.Communication.Messages;

/**
 * Describe form which place Read*******Message class should read
 */
public enum MessageSource {

    LIGHT("src/main/java/com/ptl/Communication/Messages/LightMessage/message.swap");

    private String place;

    MessageSource(String place) {
        this.place = place;
    }

    /**
     * get string value of constant
     * @return
     */
    public String get() {
        return place;
    }

}
