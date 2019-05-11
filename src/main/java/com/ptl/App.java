package com.ptl;

import com.ptl.Communication.Console.ConsoleParser;
import com.ptl.Light.Controller;
import com.ptl.Scanner.BluetoothScanner;

/**
 * Hello world!
 * No it's joke :)
 *
 * Main class of IoT
 */
public class App 
{
    public static void main( String[] args )
    {
        /*
        We cant use Printer in here, because static class has no context to send to printer
         */
        System.out.println( "Starting IoT" );
        System.out.println("Calling Bluetooth scanner");
        //new BluetoothScanner();
        System.out.println("Calling Lights Controller");
        new Controller();
        System.out.println("Starting console");
        new ConsoleParser();
    }
}
