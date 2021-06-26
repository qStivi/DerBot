package de.qStivi.commands.rpg;

import de.qStivi.*;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;

public class InventoryCommand implements ICommand {
    @Override
    public void handle(GuildMessageReceivedEvent event, String[] args, DB db, Message reply) throws SQLException, ClassNotFoundException, InterruptedException {
        var items = db.getUniqueItemIDs(event.getAuthor().getIdLong());
        db.insertItem(event.getAuthor().getIdLong(), Items.ITEM_TEST);
        db.insertItem(event.getAuthor().getIdLong(), Items.ITEM_YO);
        db.insertItem(event.getAuthor().getIdLong(), Items.ITEM_YEE);
        var embed = new EmbedBuilder();
        for (long item : items){
            embed.addField(db.)
        }
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
