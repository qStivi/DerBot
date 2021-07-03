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
        StringBuilder argsBuilder = new StringBuilder();
        for (int i = 1; i < args.length; i++) {
            argsBuilder.append(args[i]).append(" ");
        }
        var userInput = argsBuilder.toString().strip();
        try {
            var item = Items.items.stream().filter(iItem -> iItem.getDisplayName().equalsIgnoreCase(userInput)).findFirst().get();
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
