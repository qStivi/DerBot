package qStivi.commands.music;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import qStivi.ICommand;
import qStivi.audioManagers.PlayerManager;

import javax.annotation.Nonnull;

public class SkipCommand implements ICommand {
    @Override
    public void handle(GuildMessageReceivedEvent event, String[] args) {
        var hook = event.getChannel();
        PlayerManager.getINSTANCE().skip(event.getGuild());
        hook.sendMessage("Skipping...").delay(DURATION).flatMap(Message::delete).queue();
    }

    @Override
    public @Nonnull
    String getName() {
        return "skip";
    }

    @Override
    public @Nonnull
    String getDescription() {
        return "Plays next song in queue.";
    }

    @Override
    public long getXp() {
        return 3;
    }
}
