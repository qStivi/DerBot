package de.qStivi.commands.rpg;

import de.qStivi.DB;
import de.qStivi.ICommand;
import de.qStivi.items.IItem;
import de.qStivi.items.Items;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;
import java.util.List;

public class ShopCommand implements ICommand {
    @Override
    public void handle(GuildMessageReceivedEvent event, String[] args, DB db, Message reply) throws SQLException, ClassNotFoundException, InterruptedException {
        var embed = new EmbedBuilder();
        List<IItem> items = Items.ITEMS;
        for (int i = 0; i < items.size(); i++) {
            IItem item = items.get(i);
            var description = item.getDescription();
            var fieldValue = "";
            if (description.length() > 45){
                fieldValue = description.substring(0, 45) + "...\n Price: " + item.getPrice();
            } else {
                fieldValue = description + "\n Price: " + item.getPrice();
            }
            embed.addField(item.getDisplayName() + " (" + i + ")", fieldValue, true);
        }
        reply.editMessage("**__SHOP__**").queue();
        reply.editMessageEmbeds(embed.build()).queue();
    }

    @NotNull
    @Override
    public String getName() {
        return "shop";
    }

    @NotNull
    @Override
    public String getDescription() {
        return "TODO";
    }

    @Override
    public long getXp() {
        return 0;
    }
}
