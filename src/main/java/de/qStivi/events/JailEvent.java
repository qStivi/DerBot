package de.qStivi.events;

import de.qStivi.DB;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.sql.SQLException;
import java.util.Date;

public class JailEvent implements IEvent{
    @Override
    public void execute(GuildMessageReceivedEvent event, DB db, User author) throws SQLException {
        event.getMessage().reply("Go to jail. Go *directly* to jail. Do not pass go. Do not collect any money.").queue();
        db.setLastJail(new Date().getTime(), event.getAuthor().getIdLong());
    }
}
