package de.qStivi.commands.slash.skills;

import de.qStivi.commands.slash.ISlashCommand;
import de.qStivi.enitities.player.Players;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;

public class SkillsCommand implements ISlashCommand {

    @Override
    public void handle(SlashCommandInteractionEvent event) throws Exception {
        var hook = event.getHook();
        var member = event.getMember();
        if (member == null) throw new NullPointerException("Error while getting member!");
        var player = Players.getPlayer(member.getIdLong());
        var skills = player.getSkills();

        var embed = new EmbedBuilder();
        embed.setAuthor(member.getEffectiveName(), member.getEffectiveAvatarUrl(), member.getEffectiveAvatarUrl());
        embed.setTitle("Skills");
        embed.setColor(member.getColorRaw());
        embed.addField("Work Efficiency", skills.getWorkEfficiency() + 100 + "%", true);
        embed.addField("Work Speed", skills.getWorkSpeed() + 100 + "%", true);
        embed.setTimestamp(Instant.now());

        hook.editOriginalEmbeds(embed.build()).queue();

        var points = player.getAvailableSkillPoints();
        if (points > 0) {
            hook.getInteraction().getTextChannel().sendMessage("You have skill points available to spend!").setActionRow(Button.primary("levelUp", "Level UP!")).queue();
        }
    }

    @Override
    public @NotNull CommandData getCommand() {
        return Commands.slash(getName(), getDescription());
    }

    @Override
    public @NotNull String getName() {
        return "skills";
    }

    @Override
    public @NotNull String getDescription() {
        return "Level up skills to gain bonuses.";
    }
}
