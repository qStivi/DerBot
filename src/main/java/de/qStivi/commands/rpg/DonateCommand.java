//package de.qStivi.commands.rpg;
//
//import de.qStivi.DB;
//import de.qStivi.ICommand;
//import net.dv8tion.jda.api.entities.Message;
//import org.jetbrains.annotations.NotNull;
//
//import java.sql.SQLException;
//
//public class DonateCommand implements ICommand {
//    long totalXP = 0;
//
//    @Override
//    public void handle(MessageReceivedEvent event, String[] args, DB db, Message reply) throws SQLException, ClassNotFoundException {
//        var user = event.getMessage().getMentionedUsers().get(0);
//        var money = Long.parseLong(args[2]);
//        totalXP = 0;
//
//        if (money < 0) return;
//
//        if (db.getMoney(event.getAuthor().getIdLong()) < money) {
//            reply.editMessage("You don't have enough money to do that!").queue();
//            return;
//        }
//
//        db.decrementMoney(money, event.getAuthor().getIdLong());
//        db.incrementMoney(money, user.getIdLong());
//
//        reply.editMessage("You donated " + money + ":gem: to " + user.getName()).queue();
//
//        var xp = (long) Math.floor(Math.sqrt(money)) + 5;
//
//        totalXP = xp + (long) (xp * SkillsCommand.getSocialXPPMultiplier(event.getAuthor().getIdLong()));
//    }
//
//    @NotNull
//    @Override
//    public String getName() {
//        return "donate";
//    }
//
//    @NotNull
//    @Override
//    public String getDescription() {
//        return "Donate money to the poor ones.";
//    }
//
//    @Override
//    public long getXp() {
//        return totalXP;
//    }
//}
