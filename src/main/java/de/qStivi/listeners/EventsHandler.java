package de.qStivi.listeners;

import de.qStivi.DB;
import de.qStivi.events.*;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

public class EventsHandler implements IGuildMessageReceivedEvent {
    public static final List<IEvent> EVENTS = new ArrayList<>();
    private static final Logger logger = getLogger(EventsHandler.class);
    private static final double CHANCE = .8;

    public EventsHandler() {
        EVENTS.add(new GetItemEvent());
        EVENTS.add(new GetMoneyEvent());
        EVENTS.add(new LooseMoneyEvent());
        EVENTS.add(new JailEvent());
    }

    @Override
    public void handle(@NotNull GuildMessageReceivedEvent event) {
        var random = Math.random();
        if (random <= CHANCE) {
            try {
                var db = DB.getInstance();
                Collections.shuffle(EVENTS);
                EVENTS.get(0).execute(event, db, event.getAuthor());
                logger.info(EVENTS.get(0).getClass().getSimpleName() + " has been triggered.");
            } catch (SQLException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}
