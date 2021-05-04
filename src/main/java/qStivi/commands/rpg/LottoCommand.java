package qStivi.commands.rpg;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.jetbrains.annotations.NotNull;
import qStivi.ICommand;
import qStivi.db.DB;

import java.sql.SQLException;
import java.util.Date;

public class LottoCommand implements ICommand {
    @Override
    public void handle(GuildMessageReceivedEvent event, String[] args) throws SQLException, ClassNotFoundException, InterruptedException {
        var channel = event.getChannel();
        var id = event.getAuthor().getIdLong();

        long vote;
        try {
            vote = Long.parseLong(args[1]);
        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e){
            channel.sendMessage("Please enter a number from 1 to 50.").queue();
            return;
        }
        if (vote < 1 || vote > 50) {
            channel.sendMessage("Please enter a number from 1 to 50.").queue();
            return;
        }

        var db = new DB();

        try {
            var now = new Date().getTime();
            db.setLottoVote(vote, id);
            db.incrementGamePlays(getName(), 1, id);
            db.incrementCommandTimesHandled(getName(), 1, id);
            db.setCommandLastHandled(getName(), now, id);
            db.setGameLastPlayed(getName(), now, id);
            db.incrementGamePlays(getName(), 1, id);
        } catch (SQLException e){
            vote = db.getLottoVote(id);
            channel.sendMessage("You already entered the raffle. Your vote is: " + vote).queue();
            return;
        }

        channel.sendMessage("You entered the raffle.").queue();

    }

    @NotNull
    @Override
    public String getName() {
        return "lotto";
    }

    @NotNull
    @Override
    public String getDescription() {
        return "TODO";
    }

    @Override
    public long getXp() {
        return 3;
    }
}
