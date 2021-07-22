package de.qStivi.events;

import de.qStivi.DB;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.sql.SQLException;

public class GettingSickEvent implements IEvent{
    @Override
    public void execute(GuildMessageReceivedEvent event, DB db) throws SQLException {

    }
}
