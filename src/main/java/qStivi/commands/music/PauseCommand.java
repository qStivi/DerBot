package qStivi.commands.music;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import qStivi.DB;
import qStivi.ICommand;
import qStivi.audioManagers.PlayerManager;
import qStivi.commands.rpg.SkillsCommand;

import javax.annotation.Nonnull;
import java.sql.SQLException;

public class PauseCommand implements ICommand {

    private long xp;

    @Override
    public void handle(GuildMessageReceivedEvent event, String[] args, DB db, Message reply) throws SQLException, ClassNotFoundException {
        PlayerManager.getINSTANCE().pause(event.getGuild());
        reply.editMessage("Playback paused.").queue();
        xp = 0;

        xp = 3 + (long) (3 * SkillsCommand.getSocialXPPMultiplier(event.getAuthor().getIdLong()));
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
        return xp;
    }
}
