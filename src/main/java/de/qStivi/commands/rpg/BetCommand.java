package de.qStivi.commands.rpg;

import de.qStivi.sportBet.objects.Result;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.jetbrains.annotations.NotNull;
import de.qStivi.DB;
import de.qStivi.ICommand;

import java.sql.SQLException;
import java.util.Date;

public class BetCommand implements ICommand {


    @Override
    public void handle(GuildMessageReceivedEvent event, String[] args, DB db, Message reply) throws SQLException, ClassNotFoundException, InterruptedException {
        Member member = event.getMember();
        if (member == null) return;
        long id = member.getIdLong();
        StringBuilder team = new StringBuilder();
        for (int i = 2; i < args.length; i++) {
            team.append(args[i]).append(" ");
        }
        db.nPlayer(id);
        if (db.makeBet(id, Integer.parseInt(args[1]), team.toString().strip())) {
            db.incrementGamePlays("sports", 1, id);
            db.setGameLastPlayed("sports", new Date().getTime(), id);
            String actualTeam = Result.getActualTeam(team.toString().strip());
            event.getChannel().sendMessage("You bet " + Integer.parseInt(args[1]) + " on " + actualTeam).queue();
        } else {
            event.getChannel().sendMessage("That Team either does not exist or you don't have enough money!").queue();
        }
    }

    @NotNull
    @Override
    public String getName() {
        return "bet";
    }

    @NotNull
    @Override
    public String getDescription() {
        return "null";
    }

    @Override
    public long getXp() {
        return 3;
    }
}
