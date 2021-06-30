package de.qStivi;

public interface IItem {
    void use();

    String getStaticItemName();

    String getDisplayName();

    Category getCategory();

    Rarity getRarity();

    long getPrice();
}
