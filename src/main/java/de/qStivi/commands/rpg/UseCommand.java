package de.qStivi.commands.rpg;

import de.qStivi.DB;
import de.qStivi.ICommand;
import de.qStivi.items.Items;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;

public class UseCommand implements ICommand {
    @Override
    public void handle(GuildMessageReceivedEvent event, String[] args, DB db, Message reply) throws SQLException, ClassNotFoundException, InterruptedException {
        var displayName = args[1];
        Items.items.stream().filter(iItem -> iItem.getDisplayName().equalsIgnoreCase(displayName)).findFirst().get().use();
    }

    @NotNull
    @Override
    public String getName() {
        return "use";
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
