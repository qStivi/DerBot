package qStivi.commands.music;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import qStivi.ICommand;
import qStivi.audioManagers.PlayerManager;

import javax.annotation.Nonnull;

public class RepeatCommand implements ICommand {

    @Override
    public void handle(GuildMessageReceivedEvent event, String[] args) {
        var hook = event.getChannel();
        PlayerManager playerManager = PlayerManager.getINSTANCE();
        playerManager.setRepeat(event.getGuild(), !playerManager.isRepeating(event.getGuild()));
        if (playerManager.isRepeating(event.getGuild())) {
            hook.sendMessage("Repeat: ON").delay(DURATION).flatMap(Message::delete).queue();
        } else {
            hook.sendMessage("Repeat: OFF").delay(DURATION).flatMap(Message::delete).queue();
        }
    }

    @Override
    public @Nonnull
    String getName() {
        return "repeat";
    }

    @Override
    public @Nonnull
    String getDescription() {
        return "Toggles repeating for playing song";
    }

    @Override
    public long getXp() {
        return 3;
    }
}
