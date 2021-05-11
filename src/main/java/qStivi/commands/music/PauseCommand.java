package qStivi.commands.music;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import qStivi.Bot;
import qStivi.ICommand;
import qStivi.audioManagers.PlayerManager;

import javax.annotation.Nonnull;

public class PauseCommand implements ICommand {

    @Override
    public void handle(GuildMessageReceivedEvent event, String[] args) {
        var hook = event.getChannel();
        PlayerManager.getINSTANCE().pause(event.getGuild());
        hook.sendMessage("Playback paused.").queue();
    }

    @Override
    public @Nonnull
    String getName() {
        return "pause";
    }

    @Override
    public @Nonnull
    String getDescription() {
        return "Pauses music playback.";
    }

    @Override
    public long getXp() {
        return 3 * Bot.happyHour;
    }
}
