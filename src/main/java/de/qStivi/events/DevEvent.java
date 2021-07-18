package de.qStivi.events;

import de.qStivi.DB;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class DevEvent implements IEvent{
    @Override
    public void execute(GuildMessageReceivedEvent event, DB db) {
        event.getMessage().reply("DevEvent triggered").queue();
    }
}
