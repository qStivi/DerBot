package de.qStivi.items;

import de.qStivi.Category;
import de.qStivi.DB;
import de.qStivi.Rarity;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.sql.SQLException;

public class LuckPotionItem implements IItem{
    @Override
    public void use(GuildMessageReceivedEvent event, String[] args, DB db, Message reply, User author) throws SQLException, ClassNotFoundException {
        event.getChannel().sendMessage("TODO").queue();
    }

    @Override
    public String getStaticItemName() {
        return "LUCK_POTION_ITEM";
    }

    @Override
    public String getDisplayName() {
        return "Potion of Luck";
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
        return 100000;
    }

    @Override
    public String getDescription() {
        return "TODO";
    }
}
