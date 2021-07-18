package de.qStivi.listeners;

import de.qStivi.Bot;
import de.qStivi.DB;
import de.qStivi.ICommand;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.sql.SQLException;
import java.util.Date;

import static org.slf4j.LoggerFactory.getLogger;

public class Command {
    ICommand command;
    GuildMessageReceivedEvent event;
    String[] args;
    DB db = DB.getInstance();

    public Command(ICommand command, GuildMessageReceivedEvent event, String[] args) throws SQLException, ClassNotFoundException {
        this.command = command;
        this.event = event;
        this.args = args;
    }

    void handle() throws SQLException, ClassNotFoundException, InterruptedException {
        var reply = event.getMessage().reply("Loading...").complete();

        this.command.handle(this.event, this.args, this.db, reply);

        getLogger(CommandHandler.class).info(event.getAuthor().getAsTag() + " used /" + command.getName());

        var name = command.getName();
        var id = event.getAuthor().getIdLong();
        var db = DB.getInstance();

        var xp = command.getXp() * Bot.happyHour;
        db.incrementXP(xp, id);
        db.incrementCommandXP(name, xp, id);
        if (!name.equalsIgnoreCase("slots")) db.incrementCommandTimesHandled(name, 1, id);
        if (!name.equalsIgnoreCase("work")) db.setCommandLastHandled(name, new Date().getTime(), id);

        System.gc(); // Because Memory usage gets crazy after a while
    }
}