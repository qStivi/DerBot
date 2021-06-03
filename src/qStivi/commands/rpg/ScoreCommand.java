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

public class ScoreCommand implements ICommand {
    @Override
    public void handle(GuildMessageReceivedEvent event, String[] args, DB db, Message reply) throws SQLException, ClassNotFoundException, InterruptedException {
        EmbedBuilder result = new EmbedBuilder();
        ArrayList<Result> results = new ArrayList<>();
        CrawlerResult.saveInResults(results);

        result.setTitle("Results");
        String url = "https://livescore.bet3000.com/de/handball/deutschland";
        for (Result value : results) {
            if (isFinished(url, new ArrayList<>(), value.getTeam1())) {
                result.addField(value.getTeams(), value.toString(), false);
                result.addBlankField(false);
            }
        }
        if (results.size() == 0) {
            result.setDescription("There were no games today.");
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
        return "TODO";
    }

    @Override
    public long getXp() {
        return 0;
    }
}
