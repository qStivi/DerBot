//package de.qStivi.commands.rpg;
//
//import de.qStivi.DB;
//import de.qStivi.ICommand;
//import de.qStivi.sportBet.crawler.CrawlerInfo;
//import de.qStivi.sportBet.objects.Match;
//import net.dv8tion.jda.api.EmbedBuilder;
//import net.dv8tion.jda.api.entities.Message;
//import org.jetbrains.annotations.NotNull;
//
//import java.sql.SQLException;
//import java.util.ArrayList;
//
//public class InfoCommand implements ICommand {
//    @Override
//    public void handle(MessageReceivedEvent event, String[] args, DB db, Message reply) throws SQLException, ClassNotFoundException, InterruptedException {
//        EmbedBuilder info = new EmbedBuilder();
//        ArrayList<Match> matches = new ArrayList<Match>();
//        CrawlerInfo crawler = new CrawlerInfo();
//        CrawlerInfo.saveInMatches(matches);
//
//        info.setTitle("Infos");
//        for (int i = 0; i < matches.size(); i++) {
//            info.addField(matches.get(i).getTeams(), matches.get(i).toString(), false);
//            info.addBlankField(false);
//        }
//        event.getChannel().sendMessage(info.build()).queue();
//        info.clear();
//    }
//
//    @NotNull
//    @Override
//    public String getName() {
//        return "info";
//    }
//
//    @NotNull
//    @Override
//    public String getDescription() {
//        return null;
//    }
//
//    @Override
//    public long getXp() {
//        return 0;
//    }
//}
