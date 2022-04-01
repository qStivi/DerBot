package de.qStivi.commands.slash.gamble.blackjack;

public class Card {

    public Suit suit;
    public double value;
    public String emote;

    public Card(Suit suit, int value, String emote) {
        this.suit = suit;
        this.value = value;
        this.emote = emote;
    }
}
