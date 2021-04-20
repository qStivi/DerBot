package qStivi;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

class BlackJackTest {

    private final List<Card> hand1 = new ArrayList<>();
    private final List<Card> hand2 = new ArrayList<>();
    private final List<Card> hand3 = new ArrayList<>();

    @BeforeEach
    void setUp() {
        hand1.add(new Card(Suit.Clubs, 0, Emotes.ACE_OF_CLUBS));
        hand1.add(new Card(Suit.Hearts, 0, Emotes.ACE_OF_HEARTS));
        hand1.add(new Card(Suit.Diamonds, 0, Emotes.ACE_OF_DIAMONDS));
        hand1.add(new Card(Suit.Spades, 0, Emotes.ACE_OF_SPADES));


        hand2.add(new Card(Suit.Spades, 0, Emotes.ACE_OF_SPADES));
        hand2.add(new Card(Suit.Spades, 2, Emotes.ACE_OF_SPADES));
        hand2.add(new Card(Suit.Spades, 9, Emotes.ACE_OF_SPADES));

        hand3.add(new Card(Suit.Spades, 0, Emotes.ACE_OF_SPADES));
        hand3.add(new Card(Suit.Spades, 2, Emotes.ACE_OF_SPADES));
        hand3.add(new Card(Suit.Spades, 10, Emotes.ACE_OF_SPADES));
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void hit() {
        assertEquals(14, BlackJack.count(hand1));
        assertEquals(12, BlackJack.count(hand2));
        assertEquals(13, BlackJack.count(hand3));
    }

    @Test
    void stand() {
    }

    @Test
    void count() {
    }

    @Test
    void draw() {
    }
}