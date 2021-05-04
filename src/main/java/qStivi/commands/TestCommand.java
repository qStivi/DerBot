package qStivi.commands;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import qStivi.ICommand;

import javax.annotation.Nonnull;

public class TestCommand implements ICommand {

    @Override
    public void handle(GuildMessageReceivedEvent event, String[] args) {
        event.getChannel().sendMessage("k").queue();
    }

    @Override
    public @Nonnull
    String getName() {
        return "test";
    }

    @Override
    public @Nonnull
    String getDescription() {
        return "This is a test command";
    }

    @Override
    public long getXp() {
        return 0;
    }
}
