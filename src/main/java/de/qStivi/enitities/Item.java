package de.qStivi.enitities;

import de.qStivi.Rarity;

public class Item extends Entity {

    private final double value;
    private final Rarity rarity;

    private Item(String displayName, long id, double value, Rarity rarity) {
        super(displayName, id);
        this.value = value;
        this.rarity = rarity;
    }

    private double getValue() {
        return value;
    }

    private Rarity getRarity() {
        return rarity;
    }
}
