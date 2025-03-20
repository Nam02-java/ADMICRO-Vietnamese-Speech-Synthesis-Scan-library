package com.example.speech.aiservice.vn.service.console;

public enum CommandType {
//    ABORT,
//    PAUSE,
//    RESUME,
    STOP;

    public static CommandType fromString(String command) {
        try {
            return CommandType.valueOf(command.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
