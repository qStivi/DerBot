package qStivi.commands;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.jetbrains.annotations.NotNull;
import qStivi.Bot;
import qStivi.ICommand;
import qStivi.commands.rpg.SkillsCommand;

import java.sql.SQLException;

public class LeaveCommand implements ICommand {

    private long xp;

    @Override
    public void handle(GuildMessageReceivedEvent event, String[] args) throws SQLException, ClassNotFoundException {
        var hook = event.getChannel();
        event.getGuild().getAudioManager();
        event.getGuild().getAudioManager().closeAudioConnection();
        hook.sendMessage("Bye Bye").queue();

        xp = 3 + (long) (3 * SkillsCommand.getSocialXPPMultiplier(event.getAuthor().getIdLong()));
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
        return xp;
    }
}
