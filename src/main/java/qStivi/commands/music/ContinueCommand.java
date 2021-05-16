package qStivi.commands.music;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import qStivi.Bot;
import qStivi.ICommand;
import qStivi.audioManagers.PlayerManager;
import qStivi.commands.rpg.SkillsCommand;

import javax.annotation.Nonnull;
import java.sql.SQLException;

public class ContinueCommand implements ICommand {

    private long xp = 0;

    @Override
    public void handle(GuildMessageReceivedEvent event, String[] args) throws SQLException, ClassNotFoundException {
        var hook = event.getChannel();
        PlayerManager.getINSTANCE().continueTrack(event.getGuild());
        hook.sendMessage("Continuing...").queue();
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
