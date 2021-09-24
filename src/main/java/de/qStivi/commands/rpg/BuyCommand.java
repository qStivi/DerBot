package de.qStivi.commands.rpg;

import de.qStivi.DB;
import de.qStivi.ICommand;
import de.qStivi.items.Items;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.interactions.components.Button;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;
import java.util.NoSuchElementException;

public class BuyCommand implements ICommand {
    @Override
    public void handle(GuildMessageReceivedEvent event, String[] args, DB db, Message reply) throws SQLException, ClassNotFoundException, InterruptedException {
        var userInput = Integer.parseInt(args[1]);
        try {
            var item = Items.ITEMS.get(userInput);
            reply.editMessage("Do you really want to buy " + item.getDisplayName() + " for " + item.getPrice() + ":gem: ?")
                    .setActionRow(
                            Button.success("buy " + item.getStaticItemName(), "Yes"),
                            Button.danger("buy abort", "No")
                    ).complete();

        } catch (NoSuchElementException e) {
            reply.editMessage("I couldn't find any item with that name.").queue();
        }
    }

    @NotNull
    @Override
    public String getName() {
        return "buy";
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
