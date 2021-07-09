package de.qStivi.items;

import de.qStivi.Category;
import de.qStivi.DB;
import de.qStivi.Rarity;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.sql.SQLException;

public interface IItem {
    void use(GuildMessageReceivedEvent event, DB db) throws SQLException, ClassNotFoundException;

    String getStaticItemName();

    String getDisplayName();

    Category getCategory();

    Rarity getRarity();

    long getPrice();

    String getDescription();
}
