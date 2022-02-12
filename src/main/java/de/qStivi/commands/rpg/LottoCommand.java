//package de.qStivi.commands.rpg;
//
//import de.qStivi.DB;
//import de.qStivi.ICommand;
//import net.dv8tion.jda.api.entities.Message;
//import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
//import org.jetbrains.annotations.NotNull;
//
//import java.sql.SQLException;
//import java.text.DecimalFormat;
//import java.util.Date;
//
//public class LottoCommand implements ICommand {
//    private long xp;
//
//    @Override
//    public void handle(MessageReceivedEvent event, String[] args, DB db, Message reply) throws SQLException, ClassNotFoundException, InterruptedException {
//        var id = event.getAuthor().getIdLong();
//        xp = 0;
//
//        long vote;
//        if (args.length > 1) {
//            try {
//                vote = Long.parseLong(args[1]);
//            } catch (NumberFormatException e) {
//                if (args[1].equalsIgnoreCase("pool")) {
//                    sendPoolInfo(event);
//                    return;
//                }
//                reply.editMessage("Please enter a number from 1 to 50 or 'pool'").queue();
//                return;
//            }
//        } else {
//            reply.editMessage("Please enter a number from 1 to 50 or 'pool'").queue();
//            return;
//        }
//        if (vote < 1 || vote > 50) {
//            reply.editMessage("Please enter a number from 1 to 50.").queue();
//            return;
//        }
//
//        try {
//            var now = new Date().getTime();
//            db.setLottoVote(vote, id);
//            db.incrementGamePlays(getName(), 1, id);
//            db.incrementCommandTimesHandled(getName(), 1, id);
//            db.setCommandLastHandled(getName(), now, id);
//            db.setGameLastPlayed(getName(), now, id);
//            db.incrementGamePlays(getName(), 1, id);
//        } catch (SQLException e) {
//            vote = db.getLottoVote(id);
//            reply.editMessage("You already entered the raffle. Your vote is: " + vote).queue();
//            return;
//        }
//
//        reply.editMessage("You entered the raffle.").queue();
//
//
//        xp = 3 + (long) (3 * SkillsCommand.getGambleXPMultiplier(event.getAuthor().getIdLong()));
//    }
//
//    private void sendPoolInfo(GuildMessageReceivedEvent event) throws SQLException, ClassNotFoundException {
//        var db = DB.getInstance();
//        var pool = db.getLottoPool();
//        DecimalFormat formatter = new DecimalFormat();
//        event.getChannel().sendMessage("The pool contains " + formatter.format(pool) + ":gem:").queue();
//    }
//
//    @NotNull
//    @Override
//    public String getName() {
//        return "lotto";
//    }
//
//    @NotNull
//    @Override
//    public String getDescription() {
//        return "TODO";
//    }
//
//    @Override
//    public long getXp() {
//        return xp;
//    }
//}
