package main.java.qStivi.commands.rpg;

import main.java.qStivi.DB;
import main.java.qStivi.ICommand;
import main.java.qStivi.sportBet.crawler.CrawlerInfo;
import main.java.qStivi.sportBet.objects.Match;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;
import java.util.ArrayList;

public class InfoCommand implements ICommand {
    @Override
    public void handle(GuildMessageReceivedEvent event, String[] args, DB db, Message reply) throws SQLException, ClassNotFoundException, InterruptedException {
        EmbedBuilder info = new EmbedBuilder();
        ArrayList<Match> matches = new ArrayList<Match>();
        CrawlerInfo crawler = new CrawlerInfo();
        CrawlerInfo.saveInMatches(matches);

        info.setTitle("Infos");
        for (int i = 0; i < matches.size(); i++) {
            info.addField(matches.get(i).getTeams(), matches.get(i).toString(), false);
            info.addBlankField(false);
        }
        event.getChannel().sendMessage(info.build()).queue();
        info.clear();
    }

    @NotNull
    @Override
    public String getName() {
        return "info";
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
