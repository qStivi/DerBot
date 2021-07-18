package de.qStivi.events;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public interface IEvent {
    void execute(GuildMessageReceivedEvent event);
}
