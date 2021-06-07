package qStivi.commands;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import qStivi.ICommand;
import qStivi.commands.rpg.SkillsCommand;
import qStivi.DB;

import javax.annotation.Nonnull;
import java.sql.SQLException;

public class TestCommand implements ICommand {

    private long xp;

    @Override
    public void handle(GuildMessageReceivedEvent event, String[] args, DB db, Message reply) throws SQLException, ClassNotFoundException {
        reply.editMessage("k").queue();
        xp = 0;

        xp = 1 + (long) (1 * SkillsCommand.getSocialXPPMultiplier(event.getAuthor().getIdLong()));
    }

    @Override
    public @Nonnull
    String getName() {
        return "test";
    }

    @Override
    public @Nonnull
    String getDescription() {
        return "This is a test command";
    }

    @Override
    public long getXp() {
        return xp;
    }
}
