package de.qStivi.commands.slash.util;

import de.qStivi.commands.slash.ISlashCommand;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// TODO needs to be tested
public class ShutdownCommand implements ISlashCommand {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void handle(SlashCommandInteractionEvent event) {
        var member = event.getMember();
        if (member == null) return;
        if (!member.hasPermission(Permission.ADMINISTRATOR)) return;

        event.getHook().editOriginal("Shutting down...").queue();

        logger.info("Shutting down...");

        event.getJDA().shutdown();

        System.exit(0);
    }

    @Override
    public @NotNull CommandData getCommand() {
        return Commands.slash(getName(), getDescription());
    }

    @Override
    public @NotNull String getName() {
        return "shutdown";
    }

    @Override
    public @NotNull String getDescription() {
        return "Shuts down the bot.";
    }
}
