package qStivi.commands.rpg;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.jetbrains.annotations.NotNull;
import qStivi.Bot;
import qStivi.ICommand;
import qStivi.db.DB;

import java.sql.SQLException;
import java.util.concurrent.ThreadLocalRandom;

public class BegCommand implements ICommand {
    long xp = 0;

    @Override
    public void handle(GuildMessageReceivedEvent event, String[] args) throws SQLException, ClassNotFoundException {
        var luck = ThreadLocalRandom.current().nextFloat();
        var chance = .8;

        if (luck > chance) {
            var db = new DB();
            var id = event.getAuthor().getIdLong();
            var earning = ThreadLocalRandom.current().nextInt(1, 3) * Bot.happyHour;
            db.incrementMoney(earning, id);
            db.incrementCommandMoney(getName(), earning, id);
            event.getChannel().sendMessage("Someone gave you " + earning + ":gem:").queue();

            xp = 6 + (long) (6 * SkillsCommand.getGambleXPMultiplier(event.getAuthor().getIdLong()));
        } else {
            event.getChannel().sendMessage("You didn't get anything!").queue();
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
