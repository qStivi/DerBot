package de.qStivi.listeners;

import de.qStivi.events.DevEvent;
import de.qStivi.events.IEvent;
import de.qStivi.events.JailEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EventManager extends ListenerAdapter {
    private static final double CHANCE = .5;
    public static final List<IEvent> EVENTS = new ArrayList<>();

    public EventManager() {
        EVENTS.add(new DevEvent());
        EVENTS.add(new JailEvent());
    }

    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {
        if (event.getAuthor().isBot()) return;

        var random = Math.random();
        if (random <= CHANCE){
            Collections.shuffle(EVENTS);
            EVENTS.get(0).execute(event);
        }
    }
}
