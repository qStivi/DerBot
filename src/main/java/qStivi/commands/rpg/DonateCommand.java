package qStivi.commands.rpg;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.jetbrains.annotations.NotNull;
import qStivi.ICommand;
import qStivi.db.DB;

import java.util.Date;

public class DonateCommand implements ICommand {
    long xp = 0;
    @Override
    public void handle(GuildMessageReceivedEvent event, String[] args) {
        var user = event.getMessage().getMentionedUsers().get(0);
        var money = Long.parseLong(args[2]);
        var db = new DB();

        if (money < 0) return;

        if (db.selectLong("users", "money", "id", event.getAuthor().getIdLong()) < money){
            event.getChannel().sendMessage("You don't have enough money to do that!").queue();
            return;
        }

        db.decrement("users", "money", "id", event.getAuthor().getIdLong(), money);
        db.increment("users", "money", "id", user.getIdLong(), money);

        event.getChannel().sendMessage("You donated " + money + ":gem: to " + user.getName()).queue();

        var id = event.getAuthor().getIdLong();
        var seconds = db.selectLong("users", "last_worked", "id", id);
        seconds = seconds == null ? 0 : seconds;
        var millis = seconds * 1000;
        var last = new Date(millis);
        var now = new Date();
        var diff = (now.getTime() - last.getTime()) / 1000;

        if (diff > 1200) {
            xp = (long) Math.floor(Math.sqrt(money));
            db.update("users", "last_donated", "id", id, now.getTime() / 1000);
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
