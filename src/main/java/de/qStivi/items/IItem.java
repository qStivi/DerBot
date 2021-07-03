package de.qStivi.items;

import de.qStivi.Category;
import de.qStivi.Rarity;
import net.dv8tion.jda.api.events.guild.GenericGuildEvent;
import net.dv8tion.jda.api.events.message.guild.GenericGuildMessageEvent;

public interface IItem {
    void use(GenericGuildMessageEvent event);

    String getStaticItemName();

    String getDisplayName();

    Category getCategory();

    Rarity getRarity();

    long getPrice();
}
