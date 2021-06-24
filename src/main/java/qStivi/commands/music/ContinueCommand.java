package main.java.qStivi.commands.music;

import main.java.qStivi.DB;
import main.java.qStivi.ICommand;
import main.java.qStivi.audioManagers.PlayerManager;
import main.java.qStivi.commands.rpg.SkillsCommand;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import javax.annotation.Nonnull;
import java.sql.SQLException;

public class ContinueCommand implements ICommand {

    private long xp = 0;

    @Override
    public void handle(GuildMessageReceivedEvent event, String[] args, DB db, Message reply) throws SQLException, ClassNotFoundException {
        PlayerManager.getINSTANCE().continueTrack(event.getGuild());
        reply.editMessage("Continuing...").queue();
        xp = 0;

        xp = 3 + (long) (3 * SkillsCommand.getSocialXPPMultiplier(event.getAuthor().getIdLong()));
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
        return xp;
    }
}
