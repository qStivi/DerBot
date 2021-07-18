package de.qStivi.events;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class JailEvent implements IEvent{
    @Override
    public void execute(GuildMessageReceivedEvent event) {
        event.getMessage().reply("Jail event  triggered").queue();
    }
}
