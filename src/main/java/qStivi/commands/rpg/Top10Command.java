package main.java.qStivi.commands.rpg;

import main.java.qStivi.DB;
import main.java.qStivi.ICommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;
import java.util.concurrent.atomic.AtomicReference;

public class Top10Command implements ICommand {

    private long xp;

    @Override
    public void handle(GuildMessageReceivedEvent event, String[] args, DB db, Message reply) throws SQLException, ClassNotFoundException {
        var embed = new EmbedBuilder();
        xp = 0;

        var list = db.getRanking();
        var size = Math.min(list.size(), 10);
        for (int i = 0; i < size; i++) {
            Long id = list.get(i);
            var money = db.getMoney(id);
            var lvl = db.getLevel(id);

            AtomicReference<String> name = new AtomicReference<>();

            event.getJDA().retrieveUserById(id)
                    .map(User::getName)
                    .queue(name::set);

            while (name.get() == null) {
                Thread.onSpinWait();
            }
            var xp = db.getXP(id);
            var rank = i + 1;
            embed.addField("", "#" + rank + " [" + name.get() + "](https://youtu.be/dQw4w9WgXcQ) " + money + " :gem: :white_small_square: " + xp + "xp LVL: " + lvl, false);
        }
        reply.editMessage(embed.build()).queue();

        xp = 3 + (long) (3 * SkillsCommand.getSocialXPPMultiplier(event.getAuthor().getIdLong()));
    }

    @NotNull
    @Override
    public String getName() {
        return "top10";
    }

    @NotNull
    @Override
    public String getDescription() {
        return "Top 10 Players with the most money.";
    }

    @Override
    public long getXp() {
        return xp;
    }
}
