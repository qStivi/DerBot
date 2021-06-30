package de.qStivi.listeners;

import de.qStivi.Category;
import de.qStivi.DB;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.Button;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class InventoryButtonListener extends ListenerAdapter {

    @Override
    public void onButtonClick(@NotNull ButtonClickEvent event) {
        if (event.getMember().getIdLong() == event.getMessage().get)

        try {

            var items = DB.getInstance().getItems(event.getUser().getIdLong());
            var embed = new EmbedBuilder();
            var sortedItems = items.stream().filter(item -> item.getCategory().name().equals(Objects.requireNonNull(event.getButton()).getId())).toList();

            switch (Objects.requireNonNull(Objects.requireNonNull(event.getButton()).getId())) {
                case "CATEGORY1" -> {
                    event.editComponents().setActionRow(
                            Button.success(Category.CATEGORY1.name(), Category.CATEGORY1.name()).asDisabled(),
                            Button.primary(Category.CATEGORY2.name(), Category.CATEGORY2.name()),
                            Button.primary(Category.CATEGORY3.name(), Category.CATEGORY3.name()),
                            Button.primary(Category.CATEGORY4.name(), Category.CATEGORY4.name())
                    ).queue();
                    event.getHook().editOriginal(Category.CATEGORY1.name()).queue();
                }
                case "CATEGORY2" -> {
                    event.editComponents().setActionRow(
                            Button.primary(Category.CATEGORY1.name(), Category.CATEGORY1.name()),
                            Button.success(Category.CATEGORY2.name(), Category.CATEGORY2.name()).asDisabled(),
                            Button.primary(Category.CATEGORY3.name(), Category.CATEGORY3.name()),
                            Button.primary(Category.CATEGORY4.name(), Category.CATEGORY4.name())
                    ).queue();
                    event.getHook().editOriginal(Category.CATEGORY2.name()).queue();
                }
                case "CATEGORY3" -> {
                    event.editComponents().setActionRow(
                            Button.primary(Category.CATEGORY1.name(), Category.CATEGORY1.name()),
                            Button.primary(Category.CATEGORY2.name(), Category.CATEGORY2.name()),
                            Button.success(Category.CATEGORY3.name(), Category.CATEGORY3.name()).asDisabled(),
                            Button.primary(Category.CATEGORY4.name(), Category.CATEGORY4.name())
                    ).queue();
                    event.getHook().editOriginal(Category.CATEGORY3.name()).queue();
                }
                case "CATEGORY4" -> {
                    event.editComponents().setActionRow(
                            Button.primary(Category.CATEGORY1.name(), Category.CATEGORY1.name()),
                            Button.primary(Category.CATEGORY2.name(), Category.CATEGORY2.name()),
                            Button.primary(Category.CATEGORY3.name(), Category.CATEGORY3.name()),
                            Button.success(Category.CATEGORY4.name(), Category.CATEGORY4.name()).asDisabled()
                    ).queue();
                    event.getHook().editOriginal(Category.CATEGORY4.name()).queue();
                }
            }

            if (sortedItems.isEmpty()) {
                embed.addField("Empty!", "", false);
            } else {
                sortedItems.forEach(item -> embed.addField(item.getDisplayName(), String.valueOf(item.getPrice()), true));
            }

            Objects.requireNonNull(event.getMessage()).editMessageEmbeds(embed.build()).queue();

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
