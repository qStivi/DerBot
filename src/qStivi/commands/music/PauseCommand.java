package qStivi.commands.music;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import qStivi.ICommand;
import qStivi.audioManagers.PlayerManager;
import qStivi.commands.rpg.SkillsCommand;
import qStivi.db.DB;

import javax.annotation.Nonnull;
import java.sql.SQLException;

public class PauseCommand implements ICommand {

    private long xp;

    @Override
    public void handle(GuildMessageReceivedEvent event, String[] args, DB db) throws SQLException, ClassNotFoundException {
        var hook = event.getChannel();
        PlayerManager.getINSTANCE().pause(event.getGuild());
        hook.sendMessage("Playback paused.").queue();
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
