package qStivi.commands.music;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import qStivi.ICommand;
import qStivi.audioManagers.PlayerManager;

import javax.annotation.Nonnull;
import java.time.Duration;

public class ContinueCommand implements ICommand {

    @Override
    public void handle(GuildMessageReceivedEvent event, String[] args) {
        var hook = event.getChannel();
        PlayerManager.getINSTANCE().continueTrack(event.getGuild());
        hook.sendMessage("Continuing...").delay(DURATION).flatMap(Message::delete).queue();
    }

    @Override
    public @Nonnull
    String getName() {
        return "continue";
    }

    @Override
    public @Nonnull
    String getDescription() {
        return "Continues to play paused music.";
    }

    @Override
    public long getXp() {
        return 1;
    }
}
