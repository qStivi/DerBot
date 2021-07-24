package de.qStivi.commands.rpg;

import de.qStivi.Category;
import de.qStivi.DB;
import de.qStivi.ICommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.interactions.components.Button;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;
import java.util.ArrayList;

// TODO add button for all items
public class InventoryCommand implements ICommand {
    @Override
    public void handle(GuildMessageReceivedEvent event, String[] args, DB db, Message reply) throws SQLException, ClassNotFoundException, InterruptedException {
        var itemIDs = db.getItems(event.getAuthor().getIdLong());
        var embed = new EmbedBuilder();
        var sortedItemIDs = new ArrayList<Long>();
        for (long itemID : itemIDs){
            var item = db.getItem(itemID);
            if (item.getCategory().equals(Category.CATEGORY1)) sortedItemIDs.add(itemID);
        }

        if (sortedItemIDs.isEmpty()) {
            embed.addField("Empty!", "", false);
        } else {
            for (long itemID : sortedItemIDs){
                var item = db.getItem(itemID);
                embed.addField(item.getDisplayName() + " (" + itemID + ")", String.valueOf(item.getPrice()), true);
            }
        }

        reply.editMessageEmbeds(embed.build()).queue();
        reply.editMessage(Category.CATEGORY1.name()).setActionRow(
                Button.success("inv " + Category.CATEGORY1.name(), Category.CATEGORY1.name()).asDisabled(),
                Button.primary("inv " + Category.CATEGORY2.name(), Category.CATEGORY2.name()),
                Button.primary("inv " + Category.CATEGORY3.name(), Category.CATEGORY3.name()),
                Button.primary("inv " + Category.CATEGORY4.name(), Category.CATEGORY4.name())
        ).queue();
    }

    @NotNull
    @Override
    public String getName() {
        return "inv";
    }

    @NotNull
    @Override
    public String getDescription() {
        return "TODO";
    }

    @Override
    public long getXp() {
        return 3;
    }
}
