package main.java.qStivi.commands.rpg;

import main.java.qStivi.Bot;
import main.java.qStivi.DB;
import main.java.qStivi.ICommand;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;
import java.util.Date;

public class WorkCommand implements ICommand {
    long xpGain = 0;

    @Override
    public void handle(GuildMessageReceivedEvent event, String[] args, DB db, Message reply) throws SQLException, ClassNotFoundException {
        var id = event.getAuthor().getIdLong();
        xpGain = 0;


        var lvl = db.getLevel(id);

        var diff = new Date().getTime() / 1000 - db.getCommandLastHandled(getName(), id) / 1000;
        if (diff > 1200) {
            long salary = (1000 + (lvl * 10)) * Bot.happyHour;
            salary += salary * SkillsCommand.getWorkMoneyMultiplier(id);
            db.incrementMoney(salary, id);
            db.incrementCommandMoney(getName(), salary, id);
            reply.editMessage("You earned " + salary + " gems").queue();
            db.setCommandLastHandled(getName(), new Date().getTime(), id);

            xpGain = 50 + (long) (50 * SkillsCommand.getWorkXPMultiplier(event.getAuthor().getIdLong()));
        } else {
            reply.editMessage("You need to wait " + Math.subtractExact(1200L, diff) + " seconds before you can work again").queue();
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
