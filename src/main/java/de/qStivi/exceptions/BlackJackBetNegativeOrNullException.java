package de.qStivi.exceptions;

public class BlackJackBetNegativeOrNullException extends Exception {
    public BlackJackBetNegativeOrNullException() {
        super("The Bet has to be more that 0!");
    }
}
