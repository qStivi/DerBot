package qStivi.commands.rpg;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.jetbrains.annotations.NotNull;
import qStivi.ICommand;
import qStivi.db.DB;

import java.awt.*;

public class SlotsCommand implements ICommand {
    static final Symbol[] symbols = new Symbol[]{
            new Symbol(0.00564f, ":gem:", 15),
            new Symbol(0.18821f, "<:Cherry:836664853392785448>", 75),
            new Symbol(0.342f, "<:FourLeafClover:836663888480436255>", 3),
            new Symbol(0.46415f, "<:seven:836662334729617517>", 2)
    };

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
    public void handle(GuildMessageReceivedEvent event, String[] args) {
        if (event.isWebhookMessage()) return;
        var db = new DB();
        var id = event.getAuthor().getIdLong();
        var bet = Integer.parseInt(args[1]);
        var money = db.selectLong("users", "money", "id", id);
        var channel = event.getChannel();

        if (bet < 0) return;

        var first = getRandomSymbol();
        var second = getRandomSymbol();
        var third = getRandomSymbol();

        if (money < bet) {
            channel.sendMessage("Sorry but you don't have enough money to do that :(").delay(DURATION).flatMap(Message::delete).queue();
            return;
        }

        db.decrement("users", "money", "id", id, bet);

        EmbedBuilder embed = new EmbedBuilder();
        //noinspection ConstantConditions
        embed.setAuthor(event.getMember().getEffectiveName(), null, event.getAuthor().getAvatarUrl());
        embed.addField("", first.getEmote(), true);
        embed.addField("", second.getEmote(), true);
        embed.addField("", third.getEmote(), true);
        embed.setFooter(bet + "\uD83D\uDC8E");
        embed.setColor(Color.red);

        if (first.getEmote().equals(second.getEmote()) && second.getEmote().equals(third.getEmote())) {
            embed.setColor(Color.green);
            var gain = bet * first.getMultiplier();
            db.decrement("users", "money", "id", id, gain);
            channel.sendMessage("You won " + gain + ":gem:").delay(DURATION).flatMap(Message::delete).queue();
        }

        if (first.getEmote().equals(symbols[0].getEmote()) || second.getEmote().equals(symbols[0].getEmote()) || third.getEmote().equals(symbols[0].getEmote())) {
            embed.setColor(Color.green);
            var gain = bet * symbols[0].getMultiplier();
            db.decrement("users", "money", "id", id, gain);
            channel.sendMessage("You won " + gain + ":gem:").delay(DURATION).flatMap(Message::delete).queue();
        }

        if (first.getEmote().equals(symbols[0].getEmote()) && first.getEmote().equals(second.getEmote()) && second.getEmote().equals(third.getEmote())) {
            embed.setColor(Color.green);
            var gain = bet * 1000;
            db.decrement("users", "money", "id", id, gain);
            channel.sendMessage("You won " + gain + ":gem:").delay(DURATION).flatMap(Message::delete).queue();
        }

        channel.sendMessage(embed.build()).delay(DURATION).flatMap(Message::delete).queue();
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
        return 1;
    }
}

class Symbol {
    private final float weight;
    private final String emote;
    private final int multiplier;

    public Symbol(float weight, String emote, int multiplier) {
        this.weight = weight;
        this.emote = emote;
        this.multiplier = multiplier;
    }

    public float getWeight() {
        return weight;
    }

    public String getEmote() {
        return emote;
    }

    public int getMultiplier() {
        return multiplier;
    }
}