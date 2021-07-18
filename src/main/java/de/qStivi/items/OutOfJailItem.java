package de.qStivi.items;

import de.qStivi.Category;
import de.qStivi.DB;
import de.qStivi.Rarity;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.sql.SQLException;

public class OutOfJailItem implements IItem{
    @Override
    public void use(GuildMessageReceivedEvent event, String[] args, DB db, Message reply) throws SQLException, ClassNotFoundException {
        event.getChannel().sendMessage("TODO").queue();
    }

    @Override
    public String getStaticItemName() {
        return "GET_OUT_OF_JAIL_CARD_ITEM";
    }

    @Override
    public String getDisplayName() {
        return "Get out of Jail Card";
    }

    @Override
    public Category getCategory() {
        return Category.CATEGORY1;
    }

    @Override
    public Rarity getRarity() {
        return Rarity.RARE;
    }

    @Override
    public long getPrice() {
        return 123456;
    }

    @Override
    public String getDescription() {
        return "TODO";
    }
}
