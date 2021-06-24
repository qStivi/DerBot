package main.java.qStivi.commands.rpg;

import main.java.qStivi.DB;
import main.java.qStivi.ICommand;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;

public class moneyCommand implements ICommand {

    @Override
    public void handle(GuildMessageReceivedEvent event, String[] args, DB db, Message reply) throws SQLException, ClassNotFoundException {
        if (!(event.getAuthor().getIdLong() == 219108246143631364L)) {
            reply.editMessage("You don't have the permission to do that").queue();
            return;
        }

        var subcommand = args[1];
        var userID = event.getMessage().getMentionedUsers().get(0).getIdLong();
        var amount = Long.parseLong(args[3]);

        if (subcommand.equals("give")) {
//            db.increment("users", "money", "id", userID, amount);
            db.incrementMoney(amount, userID);
        }
        if (subcommand.equals("remove")) {
//            db.decrement("users", "money", "id", userID, amount);
            db.decrementMoney(amount, userID);
        }
        reply.editMessage("Done!").queue();
    }

    @NotNull
    @Override
    public String getName() {
        return "money";
    }

    @NotNull
    @Override
    public String getDescription() {
        return "Manages money.";
    }

    @Override
    public long getXp() {
        return 0;
    }
}
