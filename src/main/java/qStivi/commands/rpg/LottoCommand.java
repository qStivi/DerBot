package qStivi.commands.rpg;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.jetbrains.annotations.NotNull;
import qStivi.ICommand;
import qStivi.db.DB;

import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.Date;

public class LottoCommand implements ICommand {
    @Override
    public void handle(GuildMessageReceivedEvent event, String[] args) throws SQLException, ClassNotFoundException, InterruptedException {
        var channel = event.getChannel();
        var id = event.getAuthor().getIdLong();

        long vote;
        if (args.length > 1) {
            try {
                vote = Long.parseLong(args[1]);
            } catch (NumberFormatException e) {
                if (args[1].equalsIgnoreCase("pool")) {
                    sendPoolInfo(event);
                    return;
                }
                channel.sendMessage("Please enter a number from 1 to 50 or 'pool'").queue();
                return;
            }
        } else {
            channel.sendMessage("Please enter a number from 1 to 50 or 'pool'").queue();
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
        } catch (SQLException e) {
            vote = db.getLottoVote(id);
            channel.sendMessage("You already entered the raffle. Your vote is: " + vote).queue();
            return;
        }

        channel.sendMessage("You entered the raffle.").queue();

    }

    private void sendPoolInfo(GuildMessageReceivedEvent event) throws SQLException, ClassNotFoundException {
        var db = new DB();
        var pool = db.getLottoPool();
        DecimalFormat formatter = new DecimalFormat();
        event.getChannel().sendMessage("The pool contains " + formatter.format(pool) + ":gem:").queue();
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
