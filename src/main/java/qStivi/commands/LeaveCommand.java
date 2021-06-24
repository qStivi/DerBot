package main.java.qStivi.commands;

import main.java.qStivi.DB;
import main.java.qStivi.ICommand;
import main.java.qStivi.commands.rpg.SkillsCommand;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;

public class LeaveCommand implements ICommand {

    private long xp;

    @Override
    public void handle(GuildMessageReceivedEvent event, String[] args, DB db, Message reply) throws SQLException, ClassNotFoundException {
        xp = 0;

        event.getGuild().getAudioManager();
        event.getGuild().getAudioManager().closeAudioConnection();
        reply.editMessage("Bye Bye").queue();

        xp = 3 + (long) (3 * SkillsCommand.getSocialXPPMultiplier(event.getAuthor().getIdLong()));
    }

    @Override
    public @NotNull
    String getName() {
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
