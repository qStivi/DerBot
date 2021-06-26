package de.qStivi;

import java.util.ArrayList;
import java.util.List;

public class Items {
    List<Item> items = new ArrayList<>();

    public static final Item ITEM_TEST = new Item("ITEM_TEST", "Test item", Category.CATEGORY1, Rarity.COMMON, 5);
    public static final Item ITEM_YEE = new Item("ITEM_YEE", "Yee", Category.CATEGORY2, Rarity.MYTHICAL, 5000);
    public static final Item ITEM_YO = new Item("ITEM_YO", "YO", Category.CATEGORY4, Rarity.RARE, 50);
}
