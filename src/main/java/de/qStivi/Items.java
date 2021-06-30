package de.qStivi;

import de.qStivi.items.DevItem;

import java.util.ArrayList;
import java.util.List;

public class Items {
    public static final List<IItem> items = new ArrayList<>();

    public Items() {
        items.add(new DevItem());
    }
}
