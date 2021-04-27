package qStivi.commands.rpg;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.jetbrains.annotations.NotNull;
import qStivi.ICommand;
import qStivi.db.DB;

import java.util.concurrent.ThreadLocalRandom;

public class BegCommand implements ICommand {
    long xp = 0;

    @Override
    public void handle(GuildMessageReceivedEvent event, String[] args) {
        var luck = ThreadLocalRandom.current().nextFloat();
        var chance = .8;

        if (luck > chance) {
            var db = new DB();
            var id = event.getAuthor().getIdLong();
            var earning = ThreadLocalRandom.current().nextInt(1, 3);
            db.increment("users", "money", "id", id, earning);
            event.getChannel().sendMessage("Someone gave you " + earning + ":gem:").queue();
            xp = 6;
        } else {
            event.getChannel().sendMessage("You didn't get anything!").delay(DURATION).flatMap(Message::delete).queue();
        }
    }

    @NotNull
    @Override
    public String getName() {
        return "beg";
    }

    @NotNull
    @Override
    public String getDescription() {
        return "For the poor ones.";
    }

    @Override
    public long getXp() {
        return xp;
    }
}
