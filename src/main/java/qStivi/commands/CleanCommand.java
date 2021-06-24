package main.java.qStivi.commands;

import main.java.qStivi.DB;
import main.java.qStivi.ICommand;
import main.java.qStivi.commands.rpg.SkillsCommand;
import main.java.qStivi.listeners.CommandManager;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.slf4j.Logger;

import javax.annotation.Nonnull;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.slf4j.LoggerFactory.getLogger;

public class CleanCommand implements ICommand {
    private static final Logger logger = getLogger(CommandManager.class);
    private long xp;

    @SuppressWarnings("ConstantConditions")
    @Override
    public void handle(GuildMessageReceivedEvent event, String[] args, DB db, Message reply) throws SQLException, ClassNotFoundException {
        List<Message> messages = new ArrayList<>();
        var option = args.length > 1 && Boolean.parseBoolean(args[1]);
        xp = 0;

        if (option) {
            if (event.getMember().hasPermission(Permission.ADMINISTRATOR)) {
                messages = event.getChannel().getIterableHistory().stream().limit(1000).collect(Collectors.toList());
            } else {
                reply.editMessage("You don't have the permissions to do that.").queue();
            }
        } else {
            messages = event.getChannel().getIterableHistory().stream().limit(1000).filter(message -> message.getAuthor().getId().equals(event.getAuthor().getId())).collect(Collectors.toList());
        }
        var numberOfMessages = event.getChannel().purgeMessages(messages).size();
        logger.info(String.valueOf(numberOfMessages));

        xp = 9 + (long) (9 * SkillsCommand.getSocialXPPMultiplier(event.getAuthor().getIdLong()));
    }

    @Nonnull
    @Override
    public String getName() {
        return "clean";
    }

    @Nonnull
    @Override
    public String getDescription() {
        return "Deletes last 1000 messages. This takes quite some time.";
    }

    @Override
    public long getXp() {
        return xp;
    }
}
