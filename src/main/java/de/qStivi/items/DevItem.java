package de.qStivi.items;

import de.qStivi.*;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.slf4j.Logger;

import static org.slf4j.LoggerFactory.getLogger;

public class DevItem implements IItem {
    private static final Logger logger = getLogger(Bot.class);

    @Override
    public void use(GuildMessageReceivedEvent event, String[] args, DB db, Message reply) {
        logger.info(getStaticItemName() + " has been used.");
        event.getChannel().sendMessage("yee").queue();
    }

    @Override
    public String getStaticItemName() {
        return "DEV_ITEM";
    }

    @Override
    public String getDisplayName() {
        return "Development Item";
    }

    @Override
    public Category getCategory() {
        return Category.CATEGORY1;
    }

    @Override
    public Rarity getRarity() {
        return Rarity.COMMON;
    }

    @Override
    public long getPrice() {
        return 17;
    }

    @Override
    public String getDescription() {
        return "This is a development Item. It serves for testing purposes only and will be removed once development is finished.";
    }
}
