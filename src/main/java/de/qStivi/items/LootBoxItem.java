package de.qStivi.items;

import de.qStivi.Category;
import de.qStivi.DB;
import de.qStivi.Rarity;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.sql.SQLException;
import java.util.Collections;

public class LootBoxItem implements IItem{
    @Override
    public void use(GuildMessageReceivedEvent event, String[] args, DB db, Message reply) throws SQLException, ClassNotFoundException {
        var items = Items.ITEMS;
        Collections.shuffle(items);
        var item = items.get(0);
        db.insertItem(event.getAuthor().getIdLong(), item);
        reply.editMessage("You got one " + item.getDisplayName() + "!").queue();
    }

    @Override
    public String getStaticItemName() {
        return "LOOT_BOX_ITEM";
    }

    @Override
    public String getDisplayName() {
        return "Loot Box";
    }

    @Override
    public Category getCategory() {
        return Category.CATEGORY2;
    }

    @Override
    public Rarity getRarity() {
        return Rarity.LEGENDARY;
    }

    @Override
    public long getPrice() {
        return 10000000;
    }

    @Override
    public String getDescription() {
        return "TODO";
    }
}
