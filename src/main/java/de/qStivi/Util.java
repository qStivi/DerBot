package de.qStivi;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.interactions.Interaction;

public class Util {
    public static Member getMember(Interaction event) {
        return event.getMember();
    }
}
