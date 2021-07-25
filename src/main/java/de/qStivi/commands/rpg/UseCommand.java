package de.qStivi.commands.rpg;

import de.qStivi.DB;
import de.qStivi.ICommand;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.sql.SQLException;

import static org.slf4j.LoggerFactory.getLogger;

public class UseCommand implements ICommand {
    private static final Logger logger = getLogger(UseCommand.class);

    @Override
    public void handle(GuildMessageReceivedEvent event, String[] args, DB db, Message reply) throws SQLException, ClassNotFoundException, InterruptedException {
        var userInput = Long.parseLong(args[1]);
        var item = db.getItem(userInput);
        item.use(event, args, db, reply, event.getAuthor());
        db.removeItem(event.getAuthor().getIdLong(), userInput);
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
