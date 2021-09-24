package de.qStivi.listeners;

import de.qStivi.DB;
import de.qStivi.events.GetItemEvent;
import de.qStivi.events.GetMoneyEvent;
import de.qStivi.events.IEvent;
import de.qStivi.events.LooseMoneyEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.sql.SQLException;

import static org.slf4j.LoggerFactory.getLogger;

public class EventsHandler implements IGuildMessageReceivedEvent {
    private static final Event[] EVENTS = new Event[]{
            new Event(new GetItemEvent(), .05f),
            new Event(new GetMoneyEvent(), .4f),
            new Event(new LooseMoneyEvent(), .55f),
//            new Event(new JailEvent(), .1f)
    };
    private static final Logger logger = getLogger(EventsHandler.class);
    private static final double CHANCE = .1;


    //TODO I used this another time in Slots. Generalize it and make it a utility function.
    public static Event getRandomEvent() {

        double totalWeight = 0.0;
        for (Event i : EVENTS) {
            totalWeight += i.weight;
        }

        int idx = 0;
        for (double r = Math.random() * totalWeight; idx < EVENTS.length - 1; ++idx) {
            r -= EVENTS[idx].weight;
            if (r <= 0.0) break;
        }
        return EVENTS[idx];
    }

    @Override
    public void handle(@NotNull GuildMessageReceivedEvent event) {
        var random = Math.random();
        if (random <= CHANCE) {
            try {
                var db = DB.getInstance();
                Event randomEvent = getRandomEvent();
                randomEvent.event.execute(event, db, event.getAuthor());
                logger.info(randomEvent.event.getClass().getSimpleName() + " has been triggered.");
            } catch (SQLException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private record Event(IEvent event, float weight) {
    }
}
