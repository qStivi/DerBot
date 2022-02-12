package de.qStivi.events;

import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class ButtonTestEvent extends ListenerAdapter {
    @Override
    public void onButtonInteraction(@NotNull ButtonInteractionEvent event) {
        event.reply("ID: " + event.getButton().getId() + " Label: " + event.getButton().getLabel() + " Style: " + event.getButton().getStyle().name()).queue();
    }
}
