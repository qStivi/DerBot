package de.qStivi.commands.rpg;

import de.qStivi.Bot;
import de.qStivi.DB;
import de.qStivi.ICommand;
import de.qStivi.items.Items;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.sql.SQLException;
import java.util.NoSuchElementException;

import static org.slf4j.LoggerFactory.getLogger;

public class UseCommand implements ICommand {
    private static final Logger logger = getLogger(UseCommand.class);
    @Override
    public void handle(GuildMessageReceivedEvent event, String[] args, DB db, Message reply) throws SQLException, ClassNotFoundException, InterruptedException {
        StringBuilder argsBuilder = new StringBuilder();
        for (int i = 1; i < args.length; i++) {
            argsBuilder.append(args[i]).append(" ");
        }
        var userInput = argsBuilder.toString().strip();
        var message = "ERROR!";
        try {
            Items.items.stream().filter(iItem -> iItem.getDisplayName().equalsIgnoreCase(userInput)).findFirst().get().use(event);
            message = "Item used.";
        } catch (NoSuchElementException e) {
            message = "I couldn't find any item with that name.";
        }
        reply.editMessage(message).queue();
    }

    @NotNull
    @Override
    public String getName() {
        return "use";
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
