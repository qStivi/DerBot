package qStivi.commands.rpg;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.jetbrains.annotations.NotNull;
import qStivi.Bot;
import qStivi.ICommand;
import qStivi.db.DB;

import java.awt.*;
import java.sql.SQLException;
import java.util.Date;

public class SlotsCommand implements ICommand {
    static final Symbol[] symbols = new Symbol[]{
            new Symbol(0.0933666574889f, ":gem:", 2),
            new Symbol(0.321829794869f, "<:seven:836662334729617517>", 5),
            new Symbol(0.584803547643f, "<:Cherry:836664853392785448>", 1.5)
    };
    private long xp;

    public static Symbol getRandomSymbol() {

        double totalWeight = 0.0;
        for (Symbol i : symbols) {
            totalWeight += i.getWeight();
        }

        int idx = 0;
        for (double r = Math.random() * totalWeight; idx < symbols.length - 1; ++idx) {
            r -= symbols[idx].getWeight();
            if (r <= 0.0) break;
        }
        return symbols[idx];
    }

    @Override
    public void handle(GuildMessageReceivedEvent event, String[] args) throws SQLException, ClassNotFoundException, InterruptedException {
        xp = 0;
        if (event.isWebhookMessage()) return;
        var db = new DB();
        var id = event.getAuthor().getIdLong();
        int bet;
        try {
            bet = Integer.parseInt(args[1]);
        } catch (NumberFormatException | IndexOutOfBoundsException e) {
            return;
        }
        var money = db.getMoney(id);
        var channel = event.getChannel();

        if (bet < 0) return;

        var first = getRandomSymbol();
        var second = getRandomSymbol();
        var third = getRandomSymbol();

        if (money < bet) {
            channel.sendMessage("Sorry but you don't have enough money to do that :(").queue();
            return;
        }

        // Wait for new game
        if (new Date().getTime() / 1000 - db.getGameLastPlayed(getName(), id) / 1000 < 3) return;

        db.decrementMoney(bet, id);
        db.incrementGamePlays(getName(), 1, id);
        Thread.sleep(500);
        db.setGameLastPlayed(getName(), new Date().getTime(), id);

        EmbedBuilder embed = new EmbedBuilder();
        embed.setAuthor(event.getMember().getEffectiveName(), null, event.getAuthor().getAvatarUrl());
        embed.addField("", first.getEmote(), true);
        embed.addField("", second.getEmote(), true);
        embed.addField("", third.getEmote(), true);
        embed.setFooter(bet + "\uD83D\uDC8E");
        embed.setColor(Color.red);


        // Gem
        // Three
        if (first.getEmote().equals(symbols[0].getEmote()) && first.getEmote().equals(second.getEmote()) && second.getEmote().equals(third.getEmote())) {
            var gain = bet * 100;
            win(db, id, channel, embed, gain);
        } else

            // Gem
            //Two
            if ((first.getEmote().equals(symbols[0].getEmote()) && second.getEmote().equals(symbols[0].getEmote())) ||
                    (second.getEmote().equals(symbols[0].getEmote()) && third.getEmote().equals(symbols[0].getEmote())) ||
                    (first.getEmote().equals(symbols[0].getEmote()) && third.getEmote().equals(symbols[0].getEmote()))
            ) {
                var gain = Math.round(bet * symbols[0].getMultiplier() * 2);
                win(db, id, channel, embed, gain);
            } else

                // Gem
                // One
                if (first.getEmote().equals(symbols[0].getEmote()) || second.getEmote().equals(symbols[0].getEmote()) || third.getEmote().equals(symbols[0].getEmote())) {
                    var gain = Math.round(bet * symbols[0].getMultiplier());
                    win(db, id, channel, embed, gain);
                } else

                    // Other
                    // Three
                    if (first.getEmote().equals(second.getEmote()) && second.getEmote().equals(third.getEmote())) {
                        var gain = Math.round(bet * first.getMultiplier());
                        win(db, id, channel, embed, gain);
                    } else {
                        db.incrementGameLoses(getName(), 1, id);
                        db.incrementLottoPool(bet/2);
                    }
        channel.sendMessage(embed.build()).queue();

        xp = 3 + (long) (3 * SkillsCommand.getGambleXPMultiplier(event.getAuthor().getIdLong()));
    }

    private void win(DB db, long id, TextChannel channel, EmbedBuilder embed, long gain) throws SQLException {
        gain = gain  * Bot.happyHour;
        embed.setColor(Color.green);
        db.incrementMoney(gain, id);
        db.incrementGameWins(getName(), 1, id);
        db.incrementCommandMoney(getName(), gain, id);
        channel.sendMessage("You won " + gain + ":gem:").queue();
    }

    @NotNull
    @Override
    public String getName() {
        return "slots";
    }

    @NotNull
    @Override
    public String getDescription() {
        return "Slots";
    }

    @Override
    public long getXp() {
        return xp;
    }
}

