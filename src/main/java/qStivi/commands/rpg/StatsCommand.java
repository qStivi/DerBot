package main.java.qStivi.commands.rpg;

import main.java.qStivi.DB;
import main.java.qStivi.ICommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.sql.SQLException;

import static org.slf4j.LoggerFactory.getLogger;

public class StatsCommand implements ICommand {
    private static final Logger logger = getLogger(StatsCommand.class);
    private long totalXP;

    @Override
    public void handle(GuildMessageReceivedEvent event, String[] args, DB db, Message reply) throws SQLException, ClassNotFoundException {
        totalXP = 0;
        if (event.isWebhookMessage()) return;
        var commandUser = event.getMessage().getMentionedMembers().size() > 0 ? event.getMessage().getMentionedMembers().get(0) : null;

        var user = commandUser == null ? event.getMember() : commandUser;
        if (user == null) {
            logger.error("userId is null!");
            return;
        }
        long userID = user.getIdLong();

        var lvl = db.getLevel(userID);
        var money = db.getMoney(userID);
        var userName = user.getEffectiveName();
        var ranking = db.getRanking();
        long position = 1337;

        for (int i = 0; i < ranking.size(); i++) {
            if (ranking.get(i) == user.getIdLong()) {
                position = i + 1;
            }
        }

        var embed = new EmbedBuilder();
        embed.setColor(user.getColor());
        embed.setAuthor(userName, "https://youtu.be/dQw4w9WgXcQ", user.getUser().getEffectiveAvatarUrl());
        embed.addField("Rank", "#" + position, false);
        embed.addField("Level", String.valueOf(lvl), true);
        embed.addField("Money", money + " :gem:", true);
        var xp = db.getXP(userID);
        embed.addField("XP", String.valueOf(xp), true);

        var gameStatistics = new EmbedBuilder();
        gameStatistics.setTitle("Game statistics");
        gameStatistics.addField("", "Blackjack", false);
        gameStatistics.addField("Plays", String.valueOf(db.getGamePlays("bj", userID)), true);
        gameStatistics.addField("Wins", String.valueOf(db.getGameWins("bj", userID)), true);
        gameStatistics.addField("Loses", String.valueOf(db.getGameLoses("bj", userID)), true);
        gameStatistics.addField("", "Slots", false);
        gameStatistics.addField("Plays", String.valueOf(db.getGamePlays("slots", userID)), true);
        gameStatistics.addField("Wins", String.valueOf(db.getGameWins("slots", userID)), true);
        gameStatistics.addField("Loses", String.valueOf(db.getGameLoses("slots", userID)), true);

        reply.editMessage(embed.build()).complete();
        event.getChannel().sendMessage(gameStatistics.build()).queue();

        totalXP = 3 + (long) (3 * SkillsCommand.getSocialXPPMultiplier(event.getAuthor().getIdLong()));
    }

    @NotNull
    @Override
    public String getName() {
        return "stats";
    }

    @NotNull
    @Override
    public String getDescription() {
        return "See cool stuff.";
    }

    @Override
    public long getXp() {
        return totalXP;
    }
}
