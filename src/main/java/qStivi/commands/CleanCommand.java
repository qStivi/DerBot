package qStivi.commands;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.slf4j.Logger;
import qStivi.ICommand;
import qStivi.listeners.CommandManager;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.slf4j.LoggerFactory.getLogger;

public class CleanCommand implements ICommand {
    private static final Logger logger = getLogger(CommandManager.class);

    @SuppressWarnings("ConstantConditions")
    @Override
    public void handle(GuildMessageReceivedEvent event, String[] args) {
        var hook = event.getChannel();
        List<Message> messages = new ArrayList<>();
        var option = args.length > 1 && Boolean.parseBoolean(args[1]);
        if (option) {
            if (event.getMember().hasPermission(Permission.ADMINISTRATOR)) {
                messages = event.getChannel().getIterableHistory().stream().limit(1000).collect(Collectors.toList());
            } else {
                hook.sendMessage("You don't have the permissions to do that.").queue();
            }
        } else {
            messages = event.getChannel().getIterableHistory().stream().limit(1000).filter(message -> message.getAuthor().getId().equals(event.getAuthor().getId())).collect(Collectors.toList());
        }
        var numberOfMessages = event.getChannel().purgeMessages(messages).size();
        logger.info(String.valueOf(numberOfMessages));
        hook.sendMessage("Cleaning...").delay(DURATION).flatMap(Message::delete).queue();
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
        return 3;
    }
}
