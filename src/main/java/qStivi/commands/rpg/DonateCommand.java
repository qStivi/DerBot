package qStivi.commands.rpg;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.jetbrains.annotations.NotNull;
import qStivi.ICommand;
import qStivi.db.DB;

import java.sql.SQLException;

public class DonateCommand implements ICommand {
    long totalXP = 0;

    @Override
    public void handle(GuildMessageReceivedEvent event, String[] args) throws SQLException, ClassNotFoundException {
        var user = event.getMessage().getMentionedUsers().get(0);
        var money = Long.parseLong(args[2]);
        var db = new DB();

        if (money < 0) return;

        if (db.getMoney(event.getAuthor().getIdLong()) < money) {
            event.getChannel().sendMessage("You don't have enough money to do that!").queue();
            return;
        }

        db.decrementMoney(money, event.getAuthor().getIdLong());
        db.incrementMoney(money, user.getIdLong());

        event.getChannel().sendMessage("You donated " + money + ":gem: to " + user.getName()).queue();

        var xp = (long) Math.floor(Math.sqrt(money)) + 5;

        totalXP = xp + (long) (xp * SkillsCommand.getSocialXPPMultiplier(event.getAuthor().getIdLong()));
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
        return totalXP;
    }
}
