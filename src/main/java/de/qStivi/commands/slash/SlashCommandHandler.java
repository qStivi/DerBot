package de.qStivi.commands.slash;

import de.qStivi.commands.slash.util.ShutdownCommand;
import de.qStivi.commands.slash.util.TestCommand;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class SlashCommandHandler extends ListenerAdapter {
    private static SlashCommandHandler instance;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final List<ISlashCommand> commands = new ArrayList<>();

    private SlashCommandHandler() {
        logger.debug("Registering commands.");
        commands.add(new TestCommand());
        commands.add(new ShutdownCommand());
    }

    public static SlashCommandHandler getInstance() {
        if (instance == null) {
            instance = new SlashCommandHandler();
        }
        return instance;
    }

    public List<ISlashCommand> getCommands() {
        logger.debug("Getting commands...");
        return commands;
    }

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        logger.debug("Deferring reply...");
        event.deferReply().complete();
        logger.debug("Reply deferred!");
        for (var command : commands) {
            if (command.getCommand().getName().equals(event.getName())) {
                logger.debug("Handling command...");
                new Thread(() -> command.handle(event)).start();
                logger.debug("Command handled!");
                return;
            }
            logger.error("NO COMMAND FOUND! (Why did this happen?! This should not be possible!)");
        }
    }
}
