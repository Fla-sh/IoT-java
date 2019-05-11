package com.ptl.Communication.Console.Parsers;

import com.ptl.Communication.Console.ParsingError;

public abstract class ParserModel {
    public abstract void parse(String[] text) throws ParsingError;
    public abstract String getName();
    public abstract void getHelp();
}
