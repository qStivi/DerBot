package de.qStivi.items;

import java.util.ArrayList;
import java.util.List;

public class Items {
    public static final List<IItem> items = new ArrayList<>();

    public Items() {
        items.add(new DevItem());
    }
}
