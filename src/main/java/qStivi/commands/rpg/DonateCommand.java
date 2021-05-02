package qStivi.commands.rpg;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.jetbrains.annotations.NotNull;
import qStivi.ICommand;
import qStivi.db.DB;

import java.sql.SQLException;
import java.util.Date;

public class DonateCommand implements ICommand {
    long xp = 0;

    @Override
    public void handle(GuildMessageReceivedEvent event, String[] args) throws SQLException, ClassNotFoundException {
        var user = event.getMessage().getMentionedUsers().get(0);
        var money = Long.parseLong(args[2]);
        var db = new DB();

        if (money < 0) return;

        //noinspection ConstantConditions
        if (db.getMoney(event.getAuthor().getIdLong()) < money) {
            event.getChannel().sendMessage("You don't have enough money to do that!").queue();
            return;
        }

//        db.decrement("users", "money", "id", event.getAuthor().getIdLong(), money);
        db.decrementMoney(money, event.getAuthor().getIdLong());
//        db.increment("users", "money", "id", user.getIdLong(), money);
        db.incrementMoney(money, user.getIdLong());

        event.getChannel().sendMessage("You donated " + money + ":gem: to " + user.getName()).queue();

        var id = event.getAuthor().getIdLong();
        var diff = db.getCommandLastHandled(getName(), id);

        if (diff > 1200) {
            xp = (long) Math.floor(Math.sqrt(money)) + 5;
//            db.update("users", "last_donated", "id", id, new Date().getTime() / 1000);
            db.setCommandLastHandled(getName(), new Date().getTime(), id);
        }
    }

    @NotNull
    @Override
    public String getName() {
        return "donate";
    }

    @NotNull
    @Override
    public String getDescription() {
        return "Donate money to the poor ones.";
    }

    @Override
    public long getXp() {
        return xp;
    }
}
