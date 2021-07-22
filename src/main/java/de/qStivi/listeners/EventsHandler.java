package de.qStivi.listeners;

import de.qStivi.DB;
import de.qStivi.events.ContestWinEvent;
import de.qStivi.events.DevEvent;
import de.qStivi.events.IEvent;
import de.qStivi.events.JailEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EventsHandler implements IGuildMessageReceivedEvent {
    public static final List<IEvent> EVENTS = new ArrayList<>();
    private static final double CHANCE = .1;

    public EventsHandler() {
        EVENTS.add(new DevEvent());
        EVENTS.add(new JailEvent());
        EVENTS.add(new ContestWinEvent());
    }

    @Override
    public void handle(@NotNull GuildMessageReceivedEvent event) {
        var random = Math.random();
        if (random <= CHANCE) {
            try {
                var db = DB.getInstance();
                Collections.shuffle(EVENTS);
                EVENTS.get(0).execute(event, db);
            } catch (SQLException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}
