package de.qStivi;

public record Item(String staticItemName, String displayName, Category category, Rarity rarity, long price) {

    public void use() {
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
