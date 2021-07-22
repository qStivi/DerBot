package de.qStivi.events;

import de.qStivi.DB;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.sql.SQLException;

public class ContestWinEvent implements IEvent{
    @Override
    public void execute(GuildMessageReceivedEvent event, DB db) throws SQLException {
        var prize = (long) (Math.random() * 1000000);
        event.getMessage().reply("Congratulations you won in a race! You'll receive " + prize + ":gem:").queue();
        db.incrementMoney(prize, event.getAuthor().getIdLong());
    }
}
