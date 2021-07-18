package de.qStivi.listeners;

import de.qStivi.DB;
import de.qStivi.events.DevEvent;
import de.qStivi.events.IEvent;
import de.qStivi.events.JailEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class EventManager extends ListenerAdapter {
    public static final List<IEvent> EVENTS = new ArrayList<>();
    private static final double CHANCE = .5;

    public EventManager() {
        EVENTS.add(new DevEvent());
        EVENTS.add(new JailEvent());
    }

    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {
        if (event.getAuthor().isBot()) return;

        try {
            if (DB.getInstance().getLastJail(event.getAuthor().getIdLong()) + TimeUnit.HOURS.toMillis(1) > new Date().getTime()){
                return;
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }

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
