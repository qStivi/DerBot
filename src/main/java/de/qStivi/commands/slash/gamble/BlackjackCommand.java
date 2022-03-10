package de.qStivi.commands.slash.gamble;

import de.qStivi.Util;
import de.qStivi.commands.slash.ISlashCommand;
import de.qStivi.exceptions.BlackJackBetNegativeOrNullException;
import de.qStivi.exceptions.CommandOptionNullException;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.InteractionHook;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import org.jetbrains.annotations.NotNull;

public class BlackjackCommand implements ISlashCommand {
    @Override
    public void handle(SlashCommandInteractionEvent event) {
        try {

            var option = event.getOption("Bet");
            if (option == null) throw new CommandOptionNullException();

            var bet = option.getAsLong();
            if (bet <= 0) throw new BlackJackBetNegativeOrNullException();

            var player = Util.getMember(event);

            var hook = event.getHook();

            blackJack(bet, player, hook);

        } catch (Exception e) {
            event.getHook().editOriginal(e.getMessage()).queue();
        }
    }

    private void blackJack(long bet, Member player, InteractionHook hook) {
        // TODO Send game message with bet & playerID attached to the buttons
        hook.editOriginal(bet + " " + player.getEffectiveName()).queue();
    }

    @Override
    public @NotNull CommandData getCommand() {
        return Commands.slash(getName(), getDescription()).addOption(OptionType.INTEGER, "Bet", "How much do you want to bet?", true);
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
