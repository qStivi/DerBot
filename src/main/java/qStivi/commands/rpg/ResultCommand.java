package qStivi.commands.rpg;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.jetbrains.annotations.NotNull;
import qStivi.DB;
import qStivi.ICommand;
import qStivi.sportBet.crawler.CrawlerResult;
import qStivi.sportBet.objects.Result;

import java.sql.SQLException;
import java.util.ArrayList;

import static qStivi.sportBet.crawler.CrawlerResult.isFinished;

public class ResultCommand implements ICommand {
    @Override
    public void handle(GuildMessageReceivedEvent event, String[] args, DB db, Message reply) throws SQLException, ClassNotFoundException, InterruptedException {
        EmbedBuilder result = new EmbedBuilder();
        ArrayList<Result> results = new ArrayList<Result>();
        CrawlerResult crawler = new CrawlerResult();
        crawler.saveInResults(results);

        result.setTitle("Ergebnis");
        String url = "https://livescore.bet3000.com/de/handball/deutschland";
        for (int i = 0; i < results.size(); i++) {
            if(isFinished(url, new ArrayList<String>(), results.get(i).getTeam1())){
                result.addField(results.get(i).getTeams(), results.get(i).toString(), false);
                result.addBlankField(false);
            }
        }
        if (results.size() == 0) {
            result.setDescription("Heute gab es keine Spiele.");
        }
        event.getChannel().sendMessage(result.build()).queue();
        result.clear();
    }

    @NotNull
    @Override
    public String getName() {
        return "score";
    }

    @NotNull
    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public long getXp() {
        return 0;
    }
}
