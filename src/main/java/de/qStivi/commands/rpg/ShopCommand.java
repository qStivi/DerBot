package de.qStivi.commands.rpg;

import de.qStivi.DB;
import de.qStivi.ICommand;
import de.qStivi.items.Items;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;

public class ShopCommand implements ICommand {
    @Override
    public void handle(GuildMessageReceivedEvent event, String[] args, DB db, Message reply) throws SQLException, ClassNotFoundException, InterruptedException {
        var embed = new EmbedBuilder();
        for (var item : Items.items) {
            embed.addField(item.getDisplayName(), String.valueOf(item.getPrice()), true);
        }
        reply.editMessage("*Shop*").queue();
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
