package qStivi.commands.rpg;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.jetbrains.annotations.NotNull;
import qStivi.ICommand;
import qStivi.db.DB;

import java.sql.SQLException;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class WorkCommand implements ICommand {
    long xpGain = 0;
    Timer timer = new Timer();

    @Override
    public void handle(GuildMessageReceivedEvent event, String[] args) throws SQLException, ClassNotFoundException {
        var hook = event.getChannel();
        var id = event.getAuthor().getIdLong();
        var db = new DB();

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                event.getMessage().delete().queue();
            }
        }, 3000);

        var diff = db.getCommandLastHandled(getName(), id);
        var xp = db.getXP(id);
        xp = xp == null ? 0 : xp;
        var lvl = (long) Math.floor(xp / (double) 800);
        long salary = 1000 + (lvl * 10);

        if (diff > 1200) {
//            db.increment("users", "money", "id", id, salary);
            db.incrementMoney(salary, id);
            hook.sendMessage("You earned " + salary + " gems").delay(DURATION).flatMap(Message::delete).queue();
//            db.update("users", "last_worked", "id", id, new Date().getTime() / 1000);
            db.setCommandLastHandled(getName(), new Date().getTime(), id);
            xpGain = 50;
        } else {
            hook.sendMessage("You need to wait " + Math.subtractExact(1200L, diff) + " seconds before you can work again").delay(DURATION).flatMap(Message::delete).queue();
        }

    }

    @NotNull
    @Override
    public String getName() {
        return "work";
    }

    @NotNull
    @Override
    public String getDescription() {
        return "Get money by working.";
    }

    @Override
    public long getXp() {
        return xpGain;
    }
}
