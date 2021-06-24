package qStivi.commands.music;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import qStivi.DB;
import qStivi.ICommand;
import qStivi.audioManagers.PlayerManager;
import qStivi.commands.rpg.SkillsCommand;

import javax.annotation.Nonnull;
import java.sql.SQLException;

public class StopCommand implements ICommand {

    private long xp;

    @Override
    public void handle(GuildMessageReceivedEvent event, String[] args, DB db, Message reply) throws SQLException, ClassNotFoundException {
        PlayerManager.getINSTANCE().clearQueue(event.getGuild());
        PlayerManager.getINSTANCE().skip(event.getGuild());
        reply.editMessage("Playback stopped.").queue();
        xp = 0;

        xp = 3 + (long) (3 * SkillsCommand.getSocialXPPMultiplier(event.getAuthor().getIdLong()));
    }

    @Override
    public @Nonnull
    String getName() {
        return "stop";
    }

    @Override
    public @Nonnull
    String getDescription() {
        return "Stop music from playing and clears queue";
    }

    @Override
    public long getXp() {
        return xp;
    }
}
