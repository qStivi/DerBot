package de.qStivi.commands.slash;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

public class ClearChatCommand implements ISlashCommand {

    @Override
    public void handle(SlashCommandInteractionEvent event) throws Exception {
        var hook = event.getHook();

        var option = event.getOption("number");
        if (option == null) throw new NullPointerException("A command option was not present when it should have been!");

        int number = (int) option.getAsLong();
        if (number <= 0) {
            hook.editOriginal("Number can't be 0 or less!").queue();
            return;
        }

        var member = event.getMember();
        if (member == null) throw new NullPointerException("Error while getting member!");
        if (!member.hasPermission(Permission.MESSAGE_MANAGE)) {
            hook.editOriginal("Sorry but you are not allowed to do that.").queue();
            return;
        }

        var channel = hook.getInteraction().getTextChannel();
        var messages = channel.getIterableHistory().takeAsync(number + 1).get();
        channel.deleteMessages(messages).queue();
        channel.sendMessage("Deleting messages...").delay(5, TimeUnit.SECONDS).flatMap(Message::delete).queue();
    }

    @Override
    public @NotNull CommandData getCommand() {
        return Commands.slash(getName(), getDescription()).addOption(OptionType.INTEGER, "number", "Number of messages to be deleted");
    }

    @Override
    public @NotNull String getName() {
        return "clear";
    }

    @Override
    public @NotNull String getDescription() {
        return "Deletes messages...";
    }
}
