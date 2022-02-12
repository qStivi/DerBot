package de.qStivi.commands.slash.gamble;

import de.qStivi.commands.slash.ISlashCommand;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import org.jetbrains.annotations.NotNull;

public class BlackjackCommand implements ISlashCommand {
    @Override
    public void handle(SlashCommandInteractionEvent event) {

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
