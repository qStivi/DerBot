package main.java.qStivi.commands.music;

import main.java.qStivi.DB;
import main.java.qStivi.ICommand;
import main.java.qStivi.audioManagers.PlayerManager;
import main.java.qStivi.commands.rpg.SkillsCommand;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import javax.annotation.Nonnull;
import java.sql.SQLException;

public class RepeatCommand implements ICommand {

    private long xp;

    @Override
    public void handle(GuildMessageReceivedEvent event, String[] args, DB db, Message reply) throws SQLException, ClassNotFoundException {
        xp = 0;

        PlayerManager playerManager = PlayerManager.getINSTANCE();
        playerManager.setRepeat(event.getGuild(), !playerManager.isRepeating(event.getGuild()));
        if (playerManager.isRepeating(event.getGuild())) {
            reply.editMessage("Repeat: ON").queue();
        } else {
            reply.editMessage("Repeat: OFF").queue();
        }

        xp = 3 + (long) (3 * SkillsCommand.getSocialXPPMultiplier(event.getAuthor().getIdLong()));
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
        return xp;
    }
}
