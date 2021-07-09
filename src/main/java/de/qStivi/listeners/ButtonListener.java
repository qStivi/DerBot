package de.qStivi.listeners;

import de.qStivi.Category;
import de.qStivi.DB;
import de.qStivi.items.Items;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.Button;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.sql.SQLException;
import java.util.ArrayList;

import static org.slf4j.LoggerFactory.getLogger;

public class ButtonListener extends ListenerAdapter {
    private static final Logger logger = getLogger(ButtonListener.class);

    @Override
    public void onButtonClick(@NotNull ButtonClickEvent event) {
        try {
            event.deferEdit().complete();
            var button = event.getButton();
            var buttonIdAndPrefix = button.getId();
            var prefix = buttonIdAndPrefix.split(" ")[0];
            var buttonId = buttonIdAndPrefix.split(" ")[1];
            var user = event.getUser();
            var UserID = user.getIdLong();
            var hook = event.getHook();
            var db = DB.getInstance();

            logger.info("Button \"" + buttonIdAndPrefix + "\" was clicked by " + user.getAsTag());

            if (prefix.equals("buy")) {
                if (buttonId.equalsIgnoreCase("abort")) {
                    hook.editOriginal("Aborted!").complete();
                    hook.editOriginalComponents().setActionRow(Button.success("null", "Yes").asDisabled(), Button.danger("null", "No").asDisabled()).complete();
                } else {
                    var item = Items.getItemByStaticName(buttonId);
                    var balance = db.getMoney(UserID);
                    if (balance >= item.getPrice()) {
                        db.decrementMoney(item.getPrice(), UserID);
                        db.insertItem(UserID, item);
                        hook.editOriginal("You successfully bought " + item.getDisplayName() + " for " + item.getPrice() + ":gem:").complete();
                        hook.editOriginalComponents().setActionRow(Button.success("null", "Yes").asDisabled(), Button.danger("null", "No").asDisabled()).complete();
                    } else {
                        hook.editOriginal("Sorry, but you don't have enough money.").complete();
                        hook.editOriginalComponents().setActionRow(Button.success("null", "Yes").asDisabled(), Button.danger("null", "No").asDisabled()).complete();
                    }
                }
            }

            if (prefix.equals("inv")) {
                var message = event.getMessage();
                var owner = message.getMentionedUsers().get(0);
                if (UserID == owner.getIdLong()) {


                    var itemIDs = DB.getInstance().getItems(UserID);
                    var embed = new EmbedBuilder();
                    var sortedItemIDs = new ArrayList<Long>();
                    for (long itemID : itemIDs) {
                        var item = db.getItem(itemID);
                        if (item.getCategory().name().equals(buttonId)) sortedItemIDs.add(itemID);
                    }

                    switch (buttonId) {
                        case "CATEGORY1" -> {
                            hook.editOriginalComponents().setActionRow(
                                    Button.success("inv " + Category.CATEGORY1.name(), Category.CATEGORY1.name()).asDisabled(),
                                    Button.primary("inv " + Category.CATEGORY2.name(), Category.CATEGORY2.name()),
                                    Button.primary("inv " + Category.CATEGORY3.name(), Category.CATEGORY3.name()),
                                    Button.primary("inv " + Category.CATEGORY4.name(), Category.CATEGORY4.name())
                            ).complete();
                            event.getMessage().editMessage(Category.CATEGORY1.name()).mentionUsers(owner.getIdLong()).complete();
                        }
                        case "CATEGORY2" -> {
                            hook.editOriginalComponents().setActionRow(
                                    Button.primary("inv " + Category.CATEGORY1.name(), Category.CATEGORY1.name()),
                                    Button.success("inv " + Category.CATEGORY2.name(), Category.CATEGORY2.name()).asDisabled(),
                                    Button.primary("inv " + Category.CATEGORY3.name(), Category.CATEGORY3.name()),
                                    Button.primary("inv " + Category.CATEGORY4.name(), Category.CATEGORY4.name())
                            ).complete();
                            event.getMessage().editMessage(Category.CATEGORY2.name()).mentionUsers(owner.getIdLong()).complete();
                        }
                        case "CATEGORY3" -> {
                            hook.editOriginalComponents().setActionRow(
                                    Button.primary("inv " + Category.CATEGORY1.name(), Category.CATEGORY1.name()),
                                    Button.primary("inv " + Category.CATEGORY2.name(), Category.CATEGORY2.name()),
                                    Button.success("inv " + Category.CATEGORY3.name(), Category.CATEGORY3.name()).asDisabled(),
                                    Button.primary("inv " + Category.CATEGORY4.name(), Category.CATEGORY4.name())
                            ).complete();
                            event.getMessage().editMessage(Category.CATEGORY3.name()).mentionUsers(owner.getIdLong()).complete();
                        }
                        case "CATEGORY4" -> {
                            hook.editOriginalComponents().setActionRow(
                                    Button.primary("inv " + Category.CATEGORY1.name(), Category.CATEGORY1.name()),
                                    Button.primary("inv " + Category.CATEGORY2.name(), Category.CATEGORY2.name()),
                                    Button.primary("inv " + Category.CATEGORY3.name(), Category.CATEGORY3.name()),
                                    Button.success("inv " + Category.CATEGORY4.name(), Category.CATEGORY4.name()).asDisabled()
                            ).complete();
                            event.getMessage().editMessage(Category.CATEGORY4.name()).mentionUsers(owner.getIdLong()).complete();
                        }
                    }

                    if (sortedItemIDs.isEmpty()) {
                        embed.addField("Empty!", "", false);
                    } else {
                        for (long itemID : sortedItemIDs) {
                            var item = db.getItem(itemID);
                            embed.addField(item.getDisplayName() + " (" + itemID + ")", String.valueOf(item.getPrice()), true);
                        }
                    }

                    event.getMessage().editMessageEmbeds(embed.build()).complete();
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
