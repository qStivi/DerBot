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

public class InventoryCommand implements ICommand {
    @Override
    public void handle(GuildMessageReceivedEvent event, String[] args, DB db, Message reply) throws SQLException, ClassNotFoundException, InterruptedException {
        var items = db.getItems(event.getAuthor().getIdLong());
        var embed = new EmbedBuilder();
        var sortedItems = items.stream().filter(item -> item.getCategory() == Category.CATEGORY1).toList();

        sortedItems.forEach(item -> embed.addField(item.getDisplayName(), String.valueOf(item.getPrice()), true));

        reply.editMessageEmbeds(embed.build()).queue();
        reply.editMessage(Category.CATEGORY1.name()).setActionRow(
                Button.success(Category.CATEGORY1.name(), Category.CATEGORY1.name()).asDisabled(),
                Button.primary(Category.CATEGORY2.name(), Category.CATEGORY2.name()),
                Button.primary(Category.CATEGORY3.name(), Category.CATEGORY3.name()),
                Button.primary(Category.CATEGORY4.name(), Category.CATEGORY4.name())
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
