package com.ptl.Communication;

/**
 * Describe form which place ReadOrder class should read
 */

public enum OrderPlace {

    CONSOLE("console_order.swap"),
    HTTP("http_order.swap");

    private String place;

    OrderPlace(String place) {
        this.place = place;
    }

    public String get() {
        return place;
    }

}
