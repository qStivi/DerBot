package de.qStivi.listeners;

import de.qStivi.Bot;
import de.qStivi.DB;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class EventsPreprocessor extends ListenerAdapter {
    public static final List<IGuildMessageReceivedEvent> GUILD_MESSAGE_RECEIVED_EVENTS = new ArrayList<>();
    public static final List<IButtonClickEvent> BUTTON_CLICK_EVENTS = new ArrayList<>();

    public EventsPreprocessor() {
        BUTTON_CLICK_EVENTS.add(new ButtonClickEventHandler());
        GUILD_MESSAGE_RECEIVED_EVENTS.add(new CommandHandler());
        GUILD_MESSAGE_RECEIVED_EVENTS.add(new EventsHandler());
    }

    @Override
    public void onButtonClick(@NotNull ButtonClickEvent event) {
        try {
            if (!isUserPermitted(event)) return;
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        BUTTON_CLICK_EVENTS.forEach(iButtonClickEvent -> iButtonClickEvent.handle(event));
    }

    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {
        if (Bot.DEV_MODE) {
            if (event.getChannel().getIdLong() != Bot.DEV_CHANNEL_ID) return;
        } else {
            if (event.getChannel().getParent().getIdLong() != Bot.CATEGORY_ID) return;
        }
        try {
            if (!isUserPermitted(event)) return;
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        GUILD_MESSAGE_RECEIVED_EVENTS.forEach(iGuildMessageReceivedEvent -> iGuildMessageReceivedEvent.handle(event));
    }

    private boolean isUserPermitted(@NotNull GuildMessageReceivedEvent event) throws SQLException, ClassNotFoundException {
        var author = event.getAuthor();
        if (event.isWebhookMessage() || author.isBot()) return false;
        if (DB.getInstance().getLastJail(author.getIdLong()) + TimeUnit.MINUTES.toMillis(1) > new Date().getTime()) return false;
        return true;
    }

    private boolean isUserPermitted(@NotNull ButtonClickEvent event) throws SQLException, ClassNotFoundException {
        var author = event.getUser();
        if (author.isBot()) return false;
        if (DB.getInstance().getLastJail(author.getIdLong()) + TimeUnit.MINUTES.toMillis(1) > new Date().getTime()) return false;
        return true;
    }
}
