package de.qStivi.events;

import de.qStivi.DB;
import de.qStivi.Rarity;
import de.qStivi.items.DevItem;
import de.qStivi.items.GetOutOfJailFreeCardItem;
import de.qStivi.items.IItem;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Collectors;

public class GetItemEvent implements IEvent {
    private final static String[] MESSAGES = {
            "TODO",
            "Some text"
    };
    private final static IItem[] ITEMS = {
            new DevItem(),
            new GetOutOfJailFreeCardItem()
    };

    @Override
    public void execute(GuildMessageReceivedEvent event, DB db, User author) throws SQLException {
        var rarity = getRandomWeightedRarity();
        var item = getRandomItemByRarity(rarity);
        if (item == null) return;

        var messages = Arrays.stream(MESSAGES).collect(Collectors.toList());
        Collections.shuffle(messages);
        var message = messages.get(0);

        event.getMessage().reply(message + " You'll receive a(n) " + item.getDisplayName()).queue();

        db.insertItem(author.getIdLong(), item);
    }

    public Rarity getRandomWeightedRarity() {
        var r = (float) Math.random();
        if (r <= .0009) {
            return Rarity.MYTHICAL;
        }
        if (r <= .009) {
            return Rarity.LEGENDARY;
        }
        if (r <= .09) {
            return Rarity.RARE;
        }
        if (r <= .4) {
            return Rarity.UNCOMMON;
        }
        return Rarity.COMMON;
    }

    public IItem getRandomItemByRarity(Rarity rarity) {
        var filteredItems = Arrays.stream(ITEMS).filter(iItem -> iItem.getRarity() == rarity).toList();
        Collections.shuffle(filteredItems);
        return filteredItems.size() > 0 ? filteredItems.get(0) : null;
    }
}
