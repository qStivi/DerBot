package de.qStivi.commands.slash.gamble.blackjack;

import de.qStivi.Card;
import de.qStivi.Cards;
import de.qStivi.enitities.Player;

import java.util.ArrayList;
import java.util.List;

public class Game {

    private final List<Card> deck;
    private final List<Card> playerHand;
    private final List<Card> dealerHand;
    private final double bettingBox;
    private final Player player;

    Game(double bettingBox, Player player) {
        this.deck = new ArrayList<>();

        deck.add(Cards.ACE_OF_CLUBS);
        deck.add(Cards.TWO_OF_CLUBS);
        deck.add(Cards.THREE_OF_CLUBS);
        deck.add(Cards.FOUR_OF_CLUBS);
        deck.add(Cards.FIVE_OF_CLUBS);
        deck.add(Cards.SIX_OF_CLUBS);
        deck.add(Cards.SEVEN_OF_CLUBS);
        deck.add(Cards.EIGHT_OF_CLUBS);
        deck.add(Cards.NINE_OF_CLUBS);
        deck.add(Cards.TEN_OF_CLUBS);
        deck.add(Cards.JESTER_OF_CLUBS);
        deck.add(Cards.QUEEN_OF_CLUBS);
        deck.add(Cards.KING_OF_CLUBS);

        deck.add(Cards.ACE_OF_HEARTS);
        deck.add(Cards.TWO_OF_HEARTS);
        deck.add(Cards.THREE_OF_HEARTS);
        deck.add(Cards.FOUR_OF_HEARTS);
        deck.add(Cards.FIVE_OF_HEARTS);
        deck.add(Cards.SIX_OF_HEARTS);
        deck.add(Cards.SEVEN_OF_HEARTS);
        deck.add(Cards.EIGHT_OF_HEARTS);
        deck.add(Cards.NINE_OF_HEARTS);
        deck.add(Cards.TEN_OF_HEARTS);
        deck.add(Cards.JESTER_OF_HEARTS);
        deck.add(Cards.QUEEN_OF_HEARTS);
        deck.add(Cards.KING_OF_HEARTS);

        deck.add(Cards.ACE_OF_DIAMONDS);
        deck.add(Cards.TWO_OF_DIAMONDS);
        deck.add(Cards.THREE_OF_DIAMONDS);
        deck.add(Cards.FOUR_OF_DIAMONDS);
        deck.add(Cards.FIVE_OF_DIAMONDS);
        deck.add(Cards.SIX_OF_DIAMONDS);
        deck.add(Cards.SEVEN_OF_DIAMONDS);
        deck.add(Cards.EIGHT_OF_DIAMONDS);
        deck.add(Cards.NINE_OF_DIAMONDS);
        deck.add(Cards.TEN_OF_DIAMONDS);
        deck.add(Cards.JESTER_OF_DIAMONDS);
        deck.add(Cards.QUEEN_OF_DIAMONDS);
        deck.add(Cards.KING_OF_DIAMONDS);

        deck.add(Cards.ACE_OF_SPADES);
        deck.add(Cards.TWO_OF_SPADES);
        deck.add(Cards.THREE_OF_SPADES);
        deck.add(Cards.FOUR_OF_SPADES);
        deck.add(Cards.FIVE_OF_SPADES);
        deck.add(Cards.SIX_OF_SPADES);
        deck.add(Cards.SEVEN_OF_SPADES);
        deck.add(Cards.EIGHT_OF_SPADES);
        deck.add(Cards.NINE_OF_SPADES);
        deck.add(Cards.TEN_OF_SPADES);
        deck.add(Cards.JESTER_OF_SPADES);
        deck.add(Cards.QUEEN_OF_SPADES);
        deck.add(Cards.KING_OF_SPADES);

        this.playerHand = new ArrayList<>();
        this.dealerHand = new ArrayList<>();
        this.bettingBox = bettingBox;
        this.player = player;
    }

    public List<Card> getDeck() {
        return deck;
    }

    public List<Card> getPlayerHand() {
        return playerHand;
    }

    public List<Card> getDealerHand() {
        return dealerHand;
    }

    public double getBettingBox() {
        return bettingBox;
    }

    public Player getPlayer() {
        return player;
    }

    public WinState hasWon() {
        var playerValue = getHandValue(playerHand);
        var dealerValue = getHandValue(dealerHand);
        var diff = playerValue - dealerValue;
        if (diff == 0) return WinState.DRAW;
        if (diff < 0 && isBusted(dealerHand)) return WinState.LOOSE;
        if (diff > 0 && isBusted(playerHand)) return WinState.WIN;
        return null;
    }

    public double getHandValue(List<Card> hand) {
        double value = 0;
        for (Card card : hand) {
            value += card.value;
        }
        if (value > 21) {
            for (Card card : hand) {
                if (card.value == 11) {
                    if (value > 21) {
                        value -= 10;
                    }
                }
            }
        }
        return value;
    }

    public boolean isBusted(List<Card> hand) {
        return getHandValue(hand) > 21;
    }

}
