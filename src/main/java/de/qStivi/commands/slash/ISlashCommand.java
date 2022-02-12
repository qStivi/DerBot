package de.qStivi.commands.slash;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import org.jetbrains.annotations.NotNull;

public interface ISlashCommand {

    void handle(SlashCommandInteractionEvent event);

    @NotNull CommandData getCommand();

    @NotNull String getName();

    @NotNull String getDescription();
}
