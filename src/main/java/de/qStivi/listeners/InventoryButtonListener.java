package de.qStivi.listeners;

import de.qStivi.Category;
import de.qStivi.DB;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.Button;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;
import java.util.Objects;

public class InventoryButtonListener extends ListenerAdapter {

    @Override
    public void onButtonClick(@NotNull ButtonClickEvent event) {
        var interactingUser = event.getUser();
        var message = event.getMessage();
        var owner = message.getMentionedUsers().get(0);
        if (interactingUser.getIdLong() == owner.getIdLong())


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
                        ).complete();
                        event.getMessage().editMessage(Category.CATEGORY1.name()).mentionUsers(owner.getIdLong()).complete();
                    }
                    case "CATEGORY2" -> {
                        event.editComponents().setActionRow(
                                Button.primary(Category.CATEGORY1.name(), Category.CATEGORY1.name()),
                                Button.success(Category.CATEGORY2.name(), Category.CATEGORY2.name()).asDisabled(),
                                Button.primary(Category.CATEGORY3.name(), Category.CATEGORY3.name()),
                                Button.primary(Category.CATEGORY4.name(), Category.CATEGORY4.name())
                        ).complete();
                        event.getMessage().editMessage(Category.CATEGORY2.name()).mentionUsers(owner.getIdLong()).complete();
                    }
                    case "CATEGORY3" -> {
                        event.editComponents().setActionRow(
                                Button.primary(Category.CATEGORY1.name(), Category.CATEGORY1.name()),
                                Button.primary(Category.CATEGORY2.name(), Category.CATEGORY2.name()),
                                Button.success(Category.CATEGORY3.name(), Category.CATEGORY3.name()).asDisabled(),
                                Button.primary(Category.CATEGORY4.name(), Category.CATEGORY4.name())
                        ).complete();
                        event.getMessage().editMessage(Category.CATEGORY3.name()).mentionUsers(owner.getIdLong()).complete();
                    }
                    case "CATEGORY4" -> {
                        event.editComponents().setActionRow(
                                Button.primary(Category.CATEGORY1.name(), Category.CATEGORY1.name()),
                                Button.primary(Category.CATEGORY2.name(), Category.CATEGORY2.name()),
                                Button.primary(Category.CATEGORY3.name(), Category.CATEGORY3.name()),
                                Button.success(Category.CATEGORY4.name(), Category.CATEGORY4.name()).asDisabled()
                        ).complete();
                        event.getMessage().editMessage(Category.CATEGORY4.name()).mentionUsers(owner.getIdLong()).complete();
                    }
                }

                if (sortedItems.isEmpty()) {
                    embed.addField("Empty!", "", false);
                } else {
                    sortedItems.forEach(item -> embed.addField(item.getDisplayName(), String.valueOf(item.getPrice()), true));
                }

                Objects.requireNonNull(event.getMessage()).editMessageEmbeds(embed.build()).complete();

            } catch (SQLException | ClassNotFoundException e) {
                e.printStackTrace();
            }
    }
}
