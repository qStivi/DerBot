package de.qStivi.items;

import de.qStivi.Category;
import de.qStivi.Rarity;

public interface IItem {
    void use();

    String getStaticItemName();

    String getDisplayName();

    Category getCategory();

    Rarity getRarity();

    long getPrice();
}
