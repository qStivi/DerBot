package de.qStivi.commands.rpg;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.jetbrains.annotations.NotNull;
import de.qStivi.DB;
import de.qStivi.ICommand;

import java.sql.SQLException;

public class SalesCommand implements ICommand {
    @Override
    public void handle(GuildMessageReceivedEvent event, String[] args, DB db, Message reply) throws SQLException, ClassNotFoundException, InterruptedException {
        long id = event.getMember().getIdLong();

        try {
            db.getProfit(id);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @NotNull
    @Override
    public String getName() {
        return "sales";
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
