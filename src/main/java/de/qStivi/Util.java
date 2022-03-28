package de.qStivi;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.interactions.Interaction;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public class Util {

    public static Member getMember(Interaction event) {
        return event.getMember();
    }

    /**
     * Get a diff between two dates
     *
     * @param date1    the oldest date
     * @param date2    the newest date
     * @param timeUnit the unit in which you want the diff
     * @return the diff value, in the provided unit
     */
    public static long getDateDiff(Date date1, Date date2, TimeUnit timeUnit) {
        long diffInMillis = date2.getTime() - date1.getTime();
        return timeUnit.convert(diffInMillis, TimeUnit.MILLISECONDS);
    }
}
