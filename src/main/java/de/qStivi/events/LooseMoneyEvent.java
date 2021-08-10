package de.qStivi.events;

import de.qStivi.DB;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Collectors;

public class LooseMoneyEvent implements IEvent {
    private static final String[] MESSAGES = {
            "TODO",
            "Some Text",
            "You hurt yourself in confusion."
    };

    @Override
    public void execute(GuildMessageReceivedEvent event, DB db, User author) throws SQLException {
        var messages = Arrays.stream(MESSAGES).collect(Collectors.toList());
        Collections.shuffle(messages);
        var message = messages.get(0);

        var money = (long) (Math.random() * 100000);

        event.getMessage().reply(message + " You'll have to pay " + money + ":gem:").queue();

        var id = author.getIdLong();
        var bank = db.getMoney(id);
        if (bank >= money) {
            db.decrementMoney(money, event.getAuthor().getIdLong());
        } else {
            db.decrementMoney(bank, id);
        }
    }
}
