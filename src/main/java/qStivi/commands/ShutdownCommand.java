package qStivi.commands;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.slf4j.Logger;
import qStivi.DB;
import qStivi.ICommand;

import javax.annotation.Nonnull;

import static org.slf4j.LoggerFactory.getLogger;

public class ShutdownCommand implements ICommand {
    private static final Logger logger = getLogger(ShutdownCommand.class);

    @Override
    public void handle(GuildMessageReceivedEvent event, String[] args, DB db, Message reply) {
        var member = event.getMember();
        if (member == null) return;
        if (!member.hasPermission(Permission.ADMINISTRATOR)) return;

        reply.editMessage("Shutting down...").queue();

        logger.info("Shutting down...");

        event.getJDA().shutdownNow();

        System.exit(0);
    }

    @Override
    public @Nonnull
    String getName() {
        return "shutdown";
    }

    @Override
    public @Nonnull
    String getDescription() {
        return "Shuts down the bot.";
    }

    @Override
    public long getXp() {
        return 0;
    }
}
