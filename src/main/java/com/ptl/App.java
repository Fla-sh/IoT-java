package com.ptl;

import com.ptl.Light.Controller;
import com.ptl.Scanner.BluetoothScanner;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        /**
         * Main class of IoT
         * Printer was not used, because this not exist in static class
         */
        System.out.println( "Starting IoT" );
        System.out.println("Calling Bluetooth scanner");
        new BluetoothScanner();
        System.out.println("Calling Lights Controller");
        new Controller();
    }
}
