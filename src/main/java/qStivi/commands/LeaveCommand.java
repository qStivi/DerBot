package qStivi.commands;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.jetbrains.annotations.NotNull;
import qStivi.ICommand;

public class LeaveCommand implements ICommand {

    @Override
    public void handle(GuildMessageReceivedEvent event, String[] args) {
        var hook = event.getChannel();
        event.getGuild().getAudioManager();
        event.getGuild().getAudioManager().closeAudioConnection();
        hook.sendMessage("Bye Bye").queue();
    }

    @Override
    public @NotNull String getName() {
        return "leave";
    }

    @NotNull
    @Override
    public String getDescription() {
        return "Disconnects bot from any voice channel.";
    }

    @Override
    public long getXp() {
        return 3;
    }
}
