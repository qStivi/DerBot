//package de.qStivi.commands.rpg;
//
//import de.qStivi.DB;
//import de.qStivi.ICommand;
//import de.qStivi.sportBet.crawler.CrawlerResult;
//import de.qStivi.sportBet.objects.Result;
//import net.dv8tion.jda.api.EmbedBuilder;
//import net.dv8tion.jda.api.entities.Message;
//import org.jetbrains.annotations.NotNull;
//
//import java.sql.SQLException;
//import java.util.ArrayList;
//
//import static de.qStivi.sportBet.crawler.CrawlerResult.isFinished;
//
//public class ScoreCommand implements ICommand {
//    @Override
//    public void handle(MessageReceivedEvent event, String[] args, DB db, Message reply) throws SQLException, ClassNotFoundException, InterruptedException {
//        EmbedBuilder result = new EmbedBuilder();
//        ArrayList<Result> results = new ArrayList<>();
//        CrawlerResult.saveInResults(results);
//
//        result.setTitle("Results");
//        String url = "https://livescore.bet3000.com/de/handball/deutschland";
//        for (Result value : results) {
//            if (isFinished(url, new ArrayList<>(), value.getTeam1())) {
//                result.addField(value.getTeams(), value.toString(), false);
//                result.addBlankField(false);
//            }
//        }
//        if (results.size() == 0) {
//            result.setDescription("There were no games today.");
//        }
//        event.getChannel().sendMessage(result.build()).queue();
//        result.clear();
//    }
//
//    @NotNull
//    @Override
//    public String getName() {
//        return "score";
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
//        return 0;
//    }
//}
