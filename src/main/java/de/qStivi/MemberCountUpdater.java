package de.qStivi;

import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class MemberCountUpdater extends ListenerAdapter {

    @Override
    public void onGenericEvent(@NotNull GenericEvent event) {
        try {
            Objects.requireNonNull(Objects.requireNonNull(event.getJDA().getGuildById(703363806356701295L)).getTextChannelById(742024523502846052L)).getManager().setName(String.valueOf(Objects.requireNonNull(event.getJDA().getGuildById(703363806356701295L)).getMemberCount())).queue();
        } catch (Exception ignored) {
        }
    }
}
