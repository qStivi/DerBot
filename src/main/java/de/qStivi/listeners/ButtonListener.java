package de.qStivi.listeners;

import de.qStivi.DB;
import de.qStivi.items.Items;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.Button;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.sql.SQLException;
import java.util.Locale;

import static org.slf4j.LoggerFactory.getLogger;

public class ButtonListener extends ListenerAdapter {
    private static final Logger logger = getLogger(ButtonListener.class);

    @Override
    public void onButtonClick(@NotNull ButtonClickEvent event) {
        event.deferEdit().complete();
        var id = event.getButton().getId();
        if (id.startsWith("buy")){
            var staticItemName = id.split(" ")[1];
            if (staticItemName.equalsIgnoreCase("abort")){
                event.getMessage().editMessage("Aborted!").queue();
                event.getHook().editOriginalComponents().setActionRow(Button.success("null", "Yes").asDisabled(), Button.danger("null", "No").asDisabled()).queue();
            }else {
                var item = Items.getItemByStaticName(staticItemName);
                try {
                    var db = DB.getInstance();
                    var UserID = event.getUser().getIdLong();
                    var balance = db.getMoney(UserID);
                    if (balance >= item.getPrice()) {
                        db.decrementMoney(item.getPrice(), UserID);
                        db.insertItem(UserID, item);
                        event.getMessage().editMessage("You successfully bought " + item.getDisplayName() + " for " + item.getPrice() + ":gem:").queue();
                        event.getHook().editOriginalComponents().setActionRow(Button.success("null", "Yes").asDisabled(), Button.danger("null", "No").asDisabled()).queue();
                    } else {
                        event.getMessage().editMessage("Sorry, but you don't have enough money.").queue();
                        event.getHook().editOriginalComponents().setActionRow(Button.success("null", "Yes").asDisabled(), Button.danger("null", "No").asDisabled()).queue();
                    }
                } catch (SQLException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }

    }
}
