package de.qStivi.items;

import java.util.ArrayList;
import java.util.List;

public class Items {
    public static final List<IItem> ITEMS = new ArrayList<>();

    public Items() {
        ITEMS.add(new DevItem());
        ITEMS.add(new LuckPotionItem());
        ITEMS.add(new XPPotionItem());
        ITEMS.add(new LootBoxItem());
//        ITEMS.add(new GetOutOfJailFreeCardItem());
    }

    public static IItem getItemByDisplayName(String displayName) {
        return Items.ITEMS.stream().filter(iItem -> iItem.getDisplayName().equalsIgnoreCase(displayName)).findFirst().get();
    }

    public static IItem getItemByStaticName(String staticName) {
        return Items.ITEMS.stream().filter(iItem -> iItem.getStaticItemName().equalsIgnoreCase(staticName)).findFirst().get();
    }
}
