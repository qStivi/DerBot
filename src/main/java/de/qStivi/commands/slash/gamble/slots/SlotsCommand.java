package de.qStivi.commands.slash.gamble.slots;

import de.qStivi.commands.slash.ISlashCommand;
import de.qStivi.enitities.Players;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Random;

public class SlotsCommand implements ISlashCommand {

    private final static double HIGH_MULTIPLIER = 5;
    private final static double MEDIUM_MULTIPLIER = 2;
    private final static double LOW_MULTIPLIER = 1.5;
    private final Symbol[] symbols = new Symbol[100];

    public SlotsCommand() {
        for (int i = 0; i < 50; i++) {
            symbols[i] = new Symbol("<:Cherry:836664853392785448>", LOW_MULTIPLIER);
        }
        for (int i = 50; i < 90; i++) {
            symbols[i] = new Symbol("<:seven:836662334729617517>", MEDIUM_MULTIPLIER);
        }
        for (int i = 90; i < 100; i++) {
            symbols[i] = new Symbol(":gem:", HIGH_MULTIPLIER);
        }
    }

    @Override
    public void handle(SlashCommandInteractionEvent event) throws SQLException {

        var hook = event.getHook();

        var option = event.getOption("bet");
        if (option == null) throw new NullPointerException("Error while getting option!");
        var bet = option.getAsLong();
        if (bet <= 0) {
            hook.editOriginal("Bet can't be 0 or less!").queue();
            return;
        }

        var member = event.getMember();
        if (member == null) throw new NullPointerException("Error while getting member!");
        var player = Players.getPlayer(member.getIdLong());

        var newBalance = player.getMoney() - bet;
        if (newBalance < 0) {
            hook.editOriginal("I'm sorry but you don't have enough money to do that. Try working or sell something.").queue();
            return;
        } else {
            player.setMoney(newBalance);
        }

        var r = new Random();

        var slots = new Symbol[]{symbols[r.nextInt(100)], symbols[r.nextInt(100)], symbols[r.nextInt(100)]};

        var embed = new EmbedBuilder();
        embed.setAuthor(member.getEffectiveName(), null, member.getAvatarUrl());
        embed.addField("", slots[0].emote(), true);
        embed.addField("", slots[1].emote(), true);
        embed.addField("", slots[2].emote(), true);
        embed.setFooter(bet + "\uD83D\uDC8E");

        if (Arrays.stream(slots).anyMatch(symbol -> symbol.emote().equals(":gem:"))) {
            embed.setColor(Color.GREEN);
            embed.setTitle("YOU WON!");
            var numberOfGems = Arrays.stream(slots).filter(symbol -> symbol.emote().equals(":gem:")).count();
            player.setMoney(player.getMoney() + (bet * (HIGH_MULTIPLIER * numberOfGems)));
            player.setXp(player.getXp() + 1);
        } else if (Arrays.stream(slots).allMatch(symbol -> symbol.emote().equals("<:seven:836662334729617517>"))) {
            embed.setColor(Color.GREEN);
            embed.setTitle("YOU WON!");
            player.setMoney(player.getMoney() + (bet * (MEDIUM_MULTIPLIER)));
            player.setXp(player.getXp() + 1);
        } else if (Arrays.stream(slots).allMatch(symbol -> symbol.emote().equals("<:Cherry:836664853392785448>"))) {
            embed.setColor(Color.GREEN);
            embed.setTitle("YOU WON!");
            player.setMoney(player.getMoney() + (bet * (LOW_MULTIPLIER)));
            player.setXp(player.getXp() + 1);
        } else {
            embed.setTitle("YOU LOST!");
            embed.setColor(Color.RED);
        }

        hook.editOriginalEmbeds(embed.build()).queue();
        hook.sendMessage(String.valueOf(player.getMoney())).queue();
    }

    @Override
    public @NotNull CommandData getCommand() {
        return Commands.slash(getName(), getDescription()).addOption(OptionType.INTEGER, "bet", "How much do you want to bet?", true);
    }

    @Override
    public @NotNull String getName() {
        return "slots";
    }

    @Override
    public @NotNull String getDescription() {
        return "Play a round of Slots and maybe you win a prize.";
    }
}
