package de.qStivi.events;

import de.qStivi.DB;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Collectors;

public class GetMoneyEvent implements IEvent {
    private static final String[] MESSAGES = {
            "TODO",
            "Some Text",
            "Congratulations you won in a race!"
    };

    @Override
    public void execute(GuildMessageReceivedEvent event, DB db, User author) throws SQLException {
        var messages = Arrays.stream(MESSAGES).collect(Collectors.toList());
        Collections.shuffle(messages);
        var message = messages.get(0);

        var money = (long) (Math.random() * 1000000);

        event.getMessage().reply(message + " You'll receive " + money + ":gem:").queue();

        db.incrementMoney(money, author.getIdLong());
    }
}
