package de.qStivi;

public class Item {
    private final String staticItemName;
    private final String displayName;
    private final Category category;
    private final Rarity rarity;
    private final long price;

    public Item(String staticItemName, String displayName, Category category, Rarity rarity, long price) {
        this.staticItemName = staticItemName;
        this.displayName = displayName;
        this.category = category;
        this.rarity = rarity;
        this.price = price;
    }

    public String getStaticItemName() {
        return staticItemName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public Category getCategory() {
        return category;
    }

    public Rarity getRarity() {
        return rarity;
    }

    public long getPrice() {
        return price;
    }
}
