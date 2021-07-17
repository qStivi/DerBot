package de.qStivi.items;

import java.util.ArrayList;
import java.util.List;

public class Items {
    public static final List<IItem> items = new ArrayList<>();

    public Items() {
        items.add(new DevItem());
        items.add(new OutOfJailItem());
        items.add(new LuckPotionItem());
        items.add(new XPPotionItem());
        items.add(new DevItem());
        items.add(new DevItem());
    }

    public static IItem getItemByDisplayName(String displayName){
        return Items.items.stream().filter(iItem -> iItem.getDisplayName().equalsIgnoreCase(displayName)).findFirst().get();
    }
    public static IItem getItemByStaticName(String staticName){
        return Items.items.stream().filter(iItem -> iItem.getStaticItemName().equalsIgnoreCase(staticName)).findFirst().get();
    }
}
