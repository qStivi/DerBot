package de.qStivi;

import java.awt.*;

public class Rarities {

    public static Color getColor(Rarity rarity) {
        return switch (rarity) {
            case COMMON -> Color.GREEN;
            case UNCOMMON -> Color.BLUE;
            case RARE -> Color.MAGENTA;
            case LEGENDARY -> Color.ORANGE;
            case MYTHICAL -> Color.RED;
        };
    }

}
