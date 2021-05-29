package qStivi;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import qStivi.db.DB;

import javax.annotation.Nonnull;
import java.sql.SQLException;
import java.time.Duration;

public interface ICommand {
    void handle(GuildMessageReceivedEvent event, String[] args, DB db) throws SQLException, ClassNotFoundException, InterruptedException;

    @Nonnull
    String getName();

    @Nonnull
    String getDescription();

    long getXp();
}
