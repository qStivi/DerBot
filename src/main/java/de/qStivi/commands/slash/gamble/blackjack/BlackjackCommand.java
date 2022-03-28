package de.qStivi.commands.slash.gamble.blackjack;

import de.qStivi.Card;
import de.qStivi.commands.slash.ISlashCommand;
import de.qStivi.enitities.player.Players;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.InteractionHook;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.sql.SQLException;

public class BlackjackCommand implements ISlashCommand {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void handle(SlashCommandInteractionEvent event) throws SQLException {
        var hook = event.getHook();

        var option = event.getOption("bet");
        if (option == null) throw new NullPointerException("A command option was not present when it should have been!");

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
        Games.putGameByPlayerId(player.getId(), new Game(bet, player));
        displayGameState(Games.getGameByPlayerId(player.getId()), hook, false);
    }

    @Override
    public @NotNull CommandData getCommand() {
        return Commands.slash(getName(), getDescription()).addOption(OptionType.INTEGER, "bet", "How much do you want to bet?", true);
    }

    @Override
    public @NotNull String getName() {
        return "bj";
    }

    @Override
    public @NotNull String getDescription() {
        return "Play a round of Blackjack and try your luck.";
    }

    public static void displayGameState(Game game, InteractionHook hook, boolean end) throws SQLException {
        var embed = prepareEmbed(game);

        var winSate = game.hasWon(end);


        switch (winSate) {
            case DRAW -> draw(game, embed);
            case WIN -> win(game, embed, hook);
            case LOOSE -> loose(embed);
            default -> {
                var drawButton = Button.primary("blackjack_draw", "Draw");
                var standButton = Button.primary("blackjack_stand", "Stand");
                hook.editOriginalEmbeds(embed.build()).setActionRow(drawButton, standButton).queue();
                return;
            }
        }

        hook.editOriginalEmbeds(embed.build()).setActionRows().queue();
    }

    private static void draw(Game game, EmbedBuilder embed) throws SQLException {
        embed.setTitle("DRAW!");
        embed.setColor(Color.YELLOW);
        game.getPlayer().setMoney(game.getPlayer().getMoney() + (game.getBettingBox()));
    }

    private static void win(Game game, EmbedBuilder embed, InteractionHook hook) throws SQLException {
        embed.setTitle("YOU WON!");
        embed.setColor(Color.GREEN);
        game.getPlayer().setMoney(game.getPlayer().getMoney() + (game.getBettingBox() * 2));
        game.getPlayer().setXp(game.getPlayer().getXp() + 1);
        hook.editOriginal("You won " + game.getBettingBox() + " \uD83D\uDC8E").queue();
    }

    private static void loose(EmbedBuilder embed) {
        embed.setTitle("YOU LOST!");
        embed.setColor(Color.RED);
    }

    private static EmbedBuilder prepareEmbed(Game game) {
        var embed = new EmbedBuilder();
        embed.setFooter(game.getBettingBox() + " \uD83D\uDC8E");

        embed.addField("Dealer", String.valueOf(game.getHandValue(game.getDealerHand())), true);
        StringBuilder dealerCards = new StringBuilder();
        var cards = game.getDealerHand();
        for (Card card : cards) {
            dealerCards.append("<:").append(card.emote).append("> ");
        }
        embed.addField("", dealerCards.toString(), true);

        embed.addBlankField(false);

        embed.addField(game.getPlayer().getDisplayName(), String.valueOf(game.getHandValue(game.getPlayerHand())), true);
        StringBuilder playerCards = new StringBuilder();
        cards = game.getPlayerHand();
        for (Card card : cards) {
            playerCards.append("<:").append(card.emote).append("> ");
        }
        embed.addField("", playerCards.toString(), true);

        return embed;
    }
}
