package de.qStivi.exceptions;

public class CommandOptionNullException extends Exception {
    public CommandOptionNullException() {
        super("A command option was not present when it should have been!");
    }
}
