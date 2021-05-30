package qStivi.commands.rpg;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.jetbrains.annotations.NotNull;
import qStivi.ICommand;
import qStivi.DB;

import java.sql.SQLException;

public class DonateCommand implements ICommand {
    long totalXP = 0;

    @Override
    public void handle(GuildMessageReceivedEvent event, String[] args, DB db, Message reply) throws SQLException, ClassNotFoundException {

        User user;
        if (!event.getGuild().getMembersByEffectiveName(args[1], true).isEmpty()) {
            user = event.getGuild().getMembersByEffectiveName(args[1], true).get(0).getUser();
        } else if (!event.getMessage().getMentionedUsers().isEmpty()) {
            user = event.getMessage().getMentionedUsers().get(0);
        } else {
            reply.editMessage("I can't find the mentioned user please try a different name.").queue();
            return;
        }
        var money = 0L;
        try {
            money = Long.parseLong(args[2]);
        } catch (Exception e) {
            reply.editMessage("Please enter a valid number").queue();
            return;
        }
        totalXP = 0;

        if (money < 0) return;

        if (db.getMoney(event.getAuthor().getIdLong()) < money) {
            reply.editMessage("You don't have enough money to do that!").queue();
            return;
        }

        db.decrementMoney(money, event.getAuthor().getIdLong());
        db.incrementMoney(money, user.getIdLong());

        reply.editMessage("You donated " + money + ":gem: to " + user.getName()).queue();

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
