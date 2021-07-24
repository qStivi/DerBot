package de.qStivi.events;

import de.qStivi.DB;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.sql.SQLException;

public class GettingSickEvent implements IEvent{
    @Override
    public void execute(GuildMessageReceivedEvent event, DB db, User author) throws SQLException {
        var fee = (long) (Math.random() * 100000);
        event.getMessage().reply("You hurt yourself in confusion. Pay " + fee + ":gem: to get recovered.").queue();
        var id = author.getIdLong();
        var bank = db.getMoney(id);
        if (bank >= fee) {
            db.decrementMoney(fee, event.getAuthor().getIdLong());
        } else {
            db.decrementMoney(bank, id);
        }
    }
}
