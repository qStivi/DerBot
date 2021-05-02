package qStivi;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import javax.annotation.Nonnull;
import java.sql.SQLException;
import java.time.Duration;

public interface ICommand {
    Duration DURATION = Duration.ofMinutes(5);

    void handle(GuildMessageReceivedEvent event, String[] args) throws SQLException, ClassNotFoundException;

    @Nonnull
    String getName();

    @Nonnull
    String getDescription();

    long getXp();
}
