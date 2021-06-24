package main.java.qStivi.commands.rpg;

import main.java.qStivi.Bot;
import main.java.qStivi.DB;
import main.java.qStivi.ICommand;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;
import java.util.concurrent.ThreadLocalRandom;

public class BegCommand implements ICommand {
    long xp = 0;

    @Override
    public void handle(GuildMessageReceivedEvent event, String[] args, DB db, Message reply) throws SQLException, ClassNotFoundException {
        var luck = ThreadLocalRandom.current().nextFloat();
        var chance = .8;
        xp = 0;

        if (luck > chance) {
            var id = event.getAuthor().getIdLong();
            var earning = ThreadLocalRandom.current().nextInt(1, 3) * Bot.happyHour;
            db.incrementMoney(earning, id);
            db.incrementCommandMoney(getName(), earning, id);
            reply.editMessage("Someone gave you " + earning + ":gem:").queue();

            xp = 6 + (long) (6 * SkillsCommand.getGambleXPMultiplier(event.getAuthor().getIdLong()));
        } else {
            reply.editMessage("You didn't get anything!").queue();
        }
    }

    @NotNull
    @Override
    public String getName() {
        return "beg";
    }

    @NotNull
    @Override
    public String getDescription() {
        return "For the poor ones.";
    }

    @Override
    public long getXp() {
        return xp;
    }
}
