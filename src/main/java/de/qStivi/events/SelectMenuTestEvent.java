package de.qStivi.events;

import net.dv8tion.jda.api.events.interaction.component.SelectMenuInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.selections.SelectOption;
import org.jetbrains.annotations.NotNull;

public class SelectMenuTestEvent extends ListenerAdapter {
    @Override
    public void onSelectMenuInteraction(@NotNull SelectMenuInteractionEvent event) {
        var test = event.getSelectedOptions();
        var sb = new StringBuilder();
        for (SelectOption selectOption : test) {
            sb.append(selectOption.getLabel()).append(" | ").append(selectOption.getValue()).append(" | ").append(selectOption.getDescription()).append("\n");
        }
        event.reply(sb.toString()).queue();
    }
}
