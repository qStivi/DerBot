package qStivi.commands.rpg;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.jetbrains.annotations.NotNull;
import qStivi.DB;
import qStivi.ICommand;

import java.sql.SQLException;

public class BetCommand implements ICommand {


    @Override
    public void handle(GuildMessageReceivedEvent event, String[] args, DB db, Message reply) throws SQLException, ClassNotFoundException, InterruptedException {
            long id = event.getMember().getIdLong();
            StringBuilder team = new StringBuilder();
            for (int i = 2; i < args.length; i++) {
                team.append(args[i]).append(" ");
            }
            try {
                db.nPlayer(id);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            try {
                if(db.makeBet(id, Integer.parseInt(args[1]), team.toString().strip())){
                    event.getChannel().sendMessage("Du hast " + Integer.parseInt(args[1]) + " auf "
                            + team.toString().strip() + " gesetzt.").queue();
                }else{
                    event.getChannel().sendMessage("Entweder existiert das Team nicht oder du hast zu wenig Geld.").queue();
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
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
        return 0;
    }
}
