//package de.qStivi.commands.rpg;
//
//import de.qStivi.DB;
//import de.qStivi.ICommand;
//import net.dv8tion.jda.api.entities.Message;
//import org.jetbrains.annotations.NotNull;
//
//import java.sql.SQLException;
//
//public class moneyCommand implements ICommand {
//
//    @Override
//    public void handle(MessageReceivedEvent event, String[] args, DB db, Message reply) throws SQLException, ClassNotFoundException {
//        if (!(event.getAuthor().getIdLong() == 219108246143631364L)) {
//            reply.editMessage("You don't have the permission to do that").queue();
//            return;
//        }
//
//        var subcommand = args[1];
//        var userID = event.getMessage().getMentionedUsers().get(0).getIdLong();
//        var amount = Long.parseLong(args[3]);
//
//        if (subcommand.equals("give")) {
//            db.incrementMoney(amount, userID);
//        }
//        if (subcommand.equals("remove")) {
//            db.decrementMoney(amount, userID);
//        }
//        reply.editMessage("Done!").queue();
//    }
//
//    @NotNull
//    @Override
//    public String getName() {
//        return "money";
//    }
//
//    @NotNull
//    @Override
//    public String getDescription() {
//        return "Manages money.";
//    }
//
//    @Override
//    public long getXp() {
//        return 0;
//    }
//}
