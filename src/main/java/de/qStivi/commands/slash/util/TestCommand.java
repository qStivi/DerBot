package de.qStivi.commands.slash.util;

import com.github.koraktor.steamcondenser.exceptions.SteamCondenserException;
import com.github.koraktor.steamcondenser.steam.community.SteamId;
import de.qStivi.commands.slash.ISlashCommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.selections.SelectMenu;
import net.dv8tion.jda.api.interactions.components.selections.SelectOption;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.time.Instant;
import java.util.List;

public class TestCommand implements ISlashCommand {

//    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void handle(SlashCommandInteractionEvent event) {

        var hook = event.getHook();
        hook.editOriginal("Select menu with default").queue();

        var primaryButton = Button.primary("primary", "primary");
        var secondaryButton = Button.secondary("secondary", "secondary");
        var dangerButton = Button.danger("danger", "danger");
        var linkButton = Button.link("https://youtu.be/dQw4w9WgXcQ", "link");
        var successButton = Button.success("success", "success");

        var disabledPrimaryButton = Button.primary("primary", "primary").asDisabled();
        var disabledSecondaryButton = Button.secondary("secondary", "secondary").asDisabled();
        var disabledDangerButton = Button.danger("danger", "danger").asDisabled();
        var disabledLinkButton = Button.link("https://youtu.be/dQw4w9WgXcQ", "link").asDisabled();
        var disabledSuccessButton = Button.success("success", "success").asDisabled();

        var selectMenuOption1 = SelectOption.of("default", "default").withDescription("this is the default value");

        var selectMenuOption2 = SelectOption.of("label", "value").withDescription("description");

        var selectMenuOption3 = SelectOption.of("without description", "without description");

        var selectMenuWithDefault = SelectMenu.create("selectMenuWithDefault").addOptions(selectMenuOption1, selectMenuOption2, selectMenuOption3).setDefaultOptions(List.of(selectMenuOption1)).build();

        var selectMenuWithPlaceholder = SelectMenu.create("selectMenuWithPlaceholder").addOptions(selectMenuOption1, selectMenuOption2, selectMenuOption3).setPlaceholder("Placeholder").build();

        var selectMenuWithMultipleValues = SelectMenu.create("selectMenuWithMultipleValues").addOptions(selectMenuOption1, selectMenuOption2, selectMenuOption3).setMaxValues(2).build();

        var fieldInlineChecked = new MessageEmbed.Field("inline checked", "inline checked", true, true);
        var fieldInline = new MessageEmbed.Field("inline", "inline", true, false);
        var fieldChecked = new MessageEmbed.Field("checked", "checked", false, true);
        var field = new MessageEmbed.Field("field", "field", false, false);

        var embed = new EmbedBuilder().setColor(Color.yellow).setTitle("Title", "https://youtu.be/dQw4w9WgXcQ").setFooter("Footer", "https://i.imgur.com/92G7Xbu.gif").setAuthor("Author", "https://youtu.be/dQw4w9WgXcQ", "https://i.imgur.com/92G7Xbu.gif").setDescription("Description").setImage("https://i.imgur.com/92G7Xbu.gif").setThumbnail("https://i.imgur.com/92G7Xbu.gif").setTimestamp(Instant.now()).addField(fieldInlineChecked).addField(fieldInlineChecked).addField(fieldInlineChecked).addField(fieldInline).addField(fieldInline).addField(fieldInline).addField(fieldInline).addBlankField(true).addField(fieldInline).addBlankField(false).addField(fieldChecked).addField(fieldChecked).addField(field).addField(field).build();

        String name = "null";
        try {
            SteamId id = SteamId.create("qStivi");
            name = id.getNickname();
        } catch (SteamCondenserException e) {
            e.printStackTrace();
        }

        hook.editOriginalComponents().setActionRow(selectMenuWithDefault).queue();
        hook.getInteraction().getTextChannel().sendMessage("Select menu with placeholder").setActionRow(selectMenuWithPlaceholder).queue();
        hook.getInteraction().getTextChannel().sendMessage("Select menu with multiple").setActionRow(selectMenuWithMultipleValues).queue();

        hook.getInteraction().getTextChannel().sendMessage("Enabled buttons").setActionRow(primaryButton, secondaryButton, dangerButton, linkButton, successButton).queue();
        hook.getInteraction().getTextChannel().sendMessage("Disabled buttons").setActionRow(disabledPrimaryButton, disabledSecondaryButton, disabledDangerButton, disabledLinkButton, disabledSuccessButton).queue();

        hook.getInteraction().getTextChannel().sendMessageEmbeds(embed).queue();

        hook.getInteraction().getTextChannel().sendMessage(name).queue();
    }

    @NotNull
    @Override
    public CommandData getCommand() {

        return Commands.slash(getName(), getDescription());
    }

    @NotNull
    @Override
    public String getName() {

        return "test";
    }

    @NotNull
    @Override
    public String getDescription() {

        return "For testing purposes.";
    }
}
