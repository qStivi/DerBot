package de.qStivi.commands.slash.gamble.blackjack;

import de.qStivi.Card;
import de.qStivi.commands.slash.ISlashCommand;
import de.qStivi.enitities.Players;
import de.qStivi.exceptions.BlackJackBetNegativeOrNullException;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.InteractionHook;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.jetbrains.annotations.NotNull;

public class BlackjackCommand implements ISlashCommand {
    @Override
    public void handle(SlashCommandInteractionEvent event) {
        try {

            var option = event.getOption("Bet");
            if (option == null) throw new NullPointerException("A command option was not present when it should have been!");

            var bet = option.getAsLong();
            if (bet <= 0) throw new BlackJackBetNegativeOrNullException();

            var member = event.getMember();
            if (member == null) throw new NullPointerException("Error while getting member!");
            var player = Players.getPlayer(member.getIdLong());

            var hook = event.getHook();

            if (Games.putGameByPlayerId(player.getId(), new Game(bet, player))) {
                displayGameState(Games.getGameByPlayerId(player.getId()), hook);
            }

        } catch (Exception e) {
            event.getHook().editOriginal(e.getMessage()).queue();
        }
    }

    private void displayGameState(Game game, InteractionHook hook) {
        var embed = new EmbedBuilder();
        embed.setFooter(game.getPlayer().getDisplayName());

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

        var drawButton = Button.primary("blackjack_draw", "Draw");
        var standButton = Button.primary("blackjack_stand", "Stand");

        hook.editOriginalEmbeds(embed.build()).setActionRow(drawButton, standButton).queue();
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
}
