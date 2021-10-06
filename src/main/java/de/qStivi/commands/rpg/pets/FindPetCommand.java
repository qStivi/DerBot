package de.qStivi.commands.rpg.pets;

import de.qStivi.DB;
import de.qStivi.ICommand;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;

public class FindPetCommand implements ICommand {
    @Override
    public void handle(GuildMessageReceivedEvent event, String[] args, DB db, Message reply) throws SQLException, ClassNotFoundException, InterruptedException {

    }

    @NotNull
    @Override
    public String getName() {
        return "null";
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
