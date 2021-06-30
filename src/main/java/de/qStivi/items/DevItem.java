package de.qStivi.items;

import de.qStivi.*;
import org.slf4j.Logger;

import static org.slf4j.LoggerFactory.getLogger;

public class DevItem implements IItem {
    private static final Logger logger = getLogger(Bot.class);

    @Override
    public void use() {
        logger.info(getStaticItemName() + " has been used.");
    }

    @Override
    public String getStaticItemName() {
        return "DEV_ITEM";
    }

    @Override
    public String getDisplayName() {
        return "Development Item";
    }

    @Override
    public Category getCategory() {
        return Category.CATEGORY1;
    }

    @Override
    public Rarity getRarity() {
        return Rarity.COMMON;
    }

    @Override
    public long getPrice() {
        return 0;
    }
}
