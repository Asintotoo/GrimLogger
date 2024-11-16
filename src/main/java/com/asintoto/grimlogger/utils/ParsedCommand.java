package com.asintoto.grimlogger.utils;

import lombok.Getter;

@SuppressWarnings("all")
@Getter
public class ParsedCommand {
    private String string;
    private int threshold;
    private int interval;
    private String command;

    public ParsedCommand(String string) {
        this.string = string;

        String firstNum = string.substring(0, string.indexOf(":"));
        String secondNum = string.substring(string.indexOf(":"), command.indexOf(" "));

        this.threshold = Integer.parseInt(firstNum);
        this.interval = Integer.parseInt(secondNum.substring(1));
        this.command = string.substring(string.indexOf(" ") + 1);
    }
}
