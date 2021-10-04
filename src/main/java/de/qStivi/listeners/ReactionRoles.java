package de.qStivi.listeners;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import de.qStivi.Bot;
import de.qStivi.Emotes;
import de.qStivi.Role;
import de.qStivi.Roles;

import java.util.Objects;

import static org.slf4j.LoggerFactory.getLogger;

@SuppressWarnings("DuplicatedCode")
public class ReactionRoles extends ListenerAdapter {
    private static final Logger logger = getLogger(ReactionRoles.class);

    volatile Guild guild;
    volatile TextChannel channel;
    volatile JDA jda;

    @Override
    public void onGuildMessageReactionAdd(@NotNull GuildMessageReactionAddEvent event) {

        if (Bot.DEV_MODE) return;

        if (event.getChannel().getIdLong() == 843093823366365184L) {
            var emote = event.getReactionEmote().getName();
            switch (emote) {
                case "LoL" -> addRoleToMember(event, Roles.LOL);
                case "Minecraft" -> addRoleToMember(event, Roles.MINECRAFT);
                case "AmongUs" -> addRoleToMember(event, Roles.AMONGUS);
                case "Warzone" -> addRoleToMember(event, Roles.WARZONE);
                case "Apex" -> addRoleToMember(event, Roles.APEX);
                case "Playstation" -> addRoleToMember(event, Roles.PLAYSTATION);
                case "Bot" -> addRoleToMember(event, Roles.BOT);
                case "GMod" -> addRoleToMember(event, Roles.GMOD);
                case "Switch" -> addRoleToMember(event, Roles.SWITCH);
                case "Civ" -> addRoleToMember(event, Roles.CIV);
                case "Xbox" -> addRoleToMember(event, Roles.XBOX);
                case "ARK" -> addRoleToMember(event, Roles.ARK);
                case "GTA5" -> addRoleToMember(event, Roles.GTA);
                case "Hearthstone" -> addRoleToMember(event, Roles.HEARTHSTONE);
                case "VR" -> addRoleToMember(event, Roles.VR);
                case "RocketLeague" -> addRoleToMember(event, Roles.ROCKETLEAGUE);
                case "Shisha" -> addRoleToMember(event, Roles.SHISHA);
                case "Valorant" -> addRoleToMember(event, Roles.VALORANT);
                case "CSGO" -> addRoleToMember(event, Roles.CSGO);
                case "RainbowSixSiege" -> addRoleToMember(event, Roles.RAINBOW);
                case "Rounds" -> addRoleToMember(event, Roles.ROUNDS);
                case "Pummel" -> addRoleToMember(event, Roles.PUMMELPARTY);
                case "Satisfactory" -> addRoleToMember(event, Roles.SATISFACTORY);
                case "Fortnite" -> addRoleToMember(event, Roles.FORTNITE);
                case "ayaya" -> addRoleToMember(event, Roles.ANIME);
                case "Diablo3" -> addRoleToMember(event, Roles.DIABLO);
                case "Coding" -> addRoleToMember(event, Roles.CODING);
                case "Steam" -> addRoleToMember(event, Roles.STEAM);
                case "Epic" -> addRoleToMember(event, Roles.EPIC);
                case "Reddit" -> addRoleToMember(event, Roles.REDDIT);
                case "NewWorld" -> addRoleToMember(event, Roles.NEWWORLD);
                case "Disney+" -> addRoleToMember(event, Roles.DISNEY);
                case "Prime" -> addRoleToMember(event, Roles.PRIME);
                case "Netflix" -> addRoleToMember(event, Roles.NETFLIX);
                case "Stonks" -> addRoleToMember(event, Roles.STONKS);
                case "♟" -> addRoleToMember(event, Roles.TABLETOP);
                case "\uD83C\uDF7E" -> addRoleToMember(event, Roles.ALCOHOL);
                case "\uD83D\uDFE4" -> addRoleToMember(event, Roles.BROWN);
                case "\uD83D\uDFE2" -> addRoleToMember(event, Roles.GREEN);
                case "\uD83D\uDD35" -> addRoleToMember(event, Roles.BLUE);
                case "\uD83D\uDFE3" -> addRoleToMember(event, Roles.PURPLE);
                case "\uD83D\uDFE1" -> addRoleToMember(event, Roles.YELLOW);
                case "\uD83D\uDFE0" -> addRoleToMember(event, Roles.ORANGE);
                case "\uD83D\uDD34" -> addRoleToMember(event, Roles.RED);
            }
        }
    }

    private void addRoleToMember(@NotNull GuildMessageReactionAddEvent event, @NotNull Role role) {

        getGuildChannelJDA(event);
        guild.addRoleToMember(event.getMember(), Objects.requireNonNull(guild.getRoleById(role.getRoleID()))).queue();
    }

    private void getGuildChannelJDA(@NotNull GuildMessageReactionAddEvent event) {
        if (this.jda == null) {
            this.jda = event.getJDA();
        }
        if (guild == null) {
            guild = this.jda.getGuildById(Bot.GUILD_ID);
            while (guild == null) {
                Thread.onSpinWait();
            }
        }
        if (channel == null) {
            channel = guild.getTextChannelById(Bot.CHANNEL_ID);
            while (channel == null) {
                Thread.onSpinWait();
            }
        }
    }

    private void getGuildChannelJDA(@NotNull GuildMessageReactionRemoveEvent event) {
        if (this.jda == null) {
            this.jda = event.getJDA();
        }
        if (guild == null) {
            guild = this.jda.getGuildById(703363806356701295L);
            while (guild == null) {
                Thread.onSpinWait();
            }
        }
        if (channel == null) {
            channel = guild.getTextChannelById(843093823366365184L);
            while (channel == null) {
                Thread.onSpinWait();
            }
        }
    }

    private void getGuildChannelJDA(@NotNull ReadyEvent event) {
        if (this.jda == null) {
            this.jda = event.getJDA();
        }
        if (guild == null) {
            guild = this.jda.getGuildById(703363806356701295L);
            while (guild == null) {
                Thread.onSpinWait();
            }
        }
        if (channel == null) {
            channel = guild.getTextChannelById(843093823366365184L);
            while (channel == null) {
                Thread.onSpinWait();
            }
        }
    }

    @Override
    public void onGuildMessageReactionRemove(@NotNull GuildMessageReactionRemoveEvent event) {

        if (Bot.DEV_MODE) return;

        if (event.getChannel().getIdLong() == 843093823366365184L) {
            var emote = event.getReactionEmote().getName();
            switch (emote) {
                case "LoL" -> removeRoleFromMember(event, Roles.LOL);
                case "Minecraft" -> removeRoleFromMember(event, Roles.MINECRAFT);
                case "AmongUs" -> removeRoleFromMember(event, Roles.AMONGUS);
                case "Warzone" -> removeRoleFromMember(event, Roles.WARZONE);
                case "Apex" -> removeRoleFromMember(event, Roles.APEX);
                case "Playstation" -> removeRoleFromMember(event, Roles.PLAYSTATION);
                case "Bot" -> removeRoleFromMember(event, Roles.BOT);
                case "GMod" -> removeRoleFromMember(event, Roles.GMOD);
                case "Switch" -> removeRoleFromMember(event, Roles.SWITCH);
                case "Civ" -> removeRoleFromMember(event, Roles.CIV);
                case "Xbox" -> removeRoleFromMember(event, Roles.XBOX);
                case "ARK" -> removeRoleFromMember(event, Roles.ARK);
                case "GTA5" -> removeRoleFromMember(event, Roles.GTA);
                case "Hearthstone" -> removeRoleFromMember(event, Roles.HEARTHSTONE);
                case "VR" -> removeRoleFromMember(event, Roles.VR);
                case "RocketLeague" -> removeRoleFromMember(event, Roles.ROCKETLEAGUE);
                case "Shisha" -> removeRoleFromMember(event, Roles.SHISHA);
                case "Valorant" -> removeRoleFromMember(event, Roles.VALORANT);
                case "CSGO" -> removeRoleFromMember(event, Roles.CSGO);
                case "RainbowSixSiege" -> removeRoleFromMember(event, Roles.RAINBOW);
                case "Rounds" -> removeRoleFromMember(event, Roles.ROUNDS);
                case "Pummel" -> removeRoleFromMember(event, Roles.PUMMELPARTY);
                case "Satisfactory" -> removeRoleFromMember(event, Roles.SATISFACTORY);
                case "Fortnite" -> removeRoleFromMember(event, Roles.FORTNITE);
                case "ayaya" -> removeRoleFromMember(event, Roles.ANIME);
                case "Diablo3" -> removeRoleFromMember(event, Roles.DIABLO);
                case "Coding" -> removeRoleFromMember(event, Roles.CODING);
                case "Steam" -> removeRoleFromMember(event, Roles.STEAM);
                case "Epic" -> removeRoleFromMember(event, Roles.EPIC);
                case "Reddit" -> removeRoleFromMember(event, Roles.REDDIT);
                case "NewWorld" -> removeRoleFromMember(event, Roles.NEWWORLD);
                case "Disney" -> removeRoleFromMember(event, Roles.DISNEY);
                case "Prime" -> removeRoleFromMember(event, Roles.PRIME);
                case "Netflix" -> removeRoleFromMember(event, Roles.NETFLIX);
                case "Stonks" -> removeRoleFromMember(event, Roles.STONKS);
                case "♟" -> removeRoleFromMember(event, Roles.TABLETOP);
                case "\uD83C\uDF7E" -> removeRoleFromMember(event, Roles.ALCOHOL);
                case "\uD83D\uDFE4" -> removeRoleFromMember(event, Roles.BROWN);
                case "\uD83D\uDFE2" -> removeRoleFromMember(event, Roles.GREEN);
                case "\uD83D\uDD35" -> removeRoleFromMember(event, Roles.BLUE);
                case "\uD83D\uDFE3" -> removeRoleFromMember(event, Roles.PURPLE);
                case "\uD83D\uDFE1" -> removeRoleFromMember(event, Roles.YELLOW);
                case "\uD83D\uDFE0" -> removeRoleFromMember(event, Roles.ORANGE);
                case "\uD83D\uDD34" -> removeRoleFromMember(event, Roles.RED);
            }
        }
    }

    private void removeRoleFromMember(@NotNull GuildMessageReactionRemoveEvent event, @NotNull Role role) {

        getGuildChannelJDA(event);
        guild.removeRoleFromMember(Objects.requireNonNull(event.getMember()), Objects.requireNonNull(guild.getRoleById(role.getRoleID()))).queue();
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public void onReady(@NotNull ReadyEvent event) {

        if (Bot.DEV_MODE) return;
        getGuildChannelJDA(event);

        if (guild == null) {
            logger.error("Guild is null!");
            return;
        }

        EmbedBuilder games = new EmbedBuilder();
        games.setTitle("Video Games");
        games.setFooter("Video Games", jda.getSelfUser().getAvatarUrl());
        addField(event, games, Roles.LOL);
        addField(event, games, Roles.MINECRAFT);
        addField(event, games, Roles.WARZONE);
        addField(event, games, Roles.APEX);
        addField(event, games, Roles.GMOD);
        addField(event, games, Roles.CIV);
        addField(event, games, Roles.ARK);
        addField(event, games, Roles.GTA);
        addField(event, games, Roles.HEARTHSTONE);
        addField(event, games, Roles.ROCKETLEAGUE);
        addField(event, games, Roles.AMONGUS);
        addField(event, games, Roles.VALORANT);
        addField(event, games, Roles.CSGO);
        addField(event, games, Roles.RAINBOW);
        addField(event, games, Roles.ROUNDS);
        addField(event, games, Roles.PUMMELPARTY);
        addField(event, games, Roles.SATISFACTORY);
        addField(event, games, Roles.FORTNITE);
        games.addField(Roles.TABLETOP.getEmoteID(), guild.getRoleById(Roles.TABLETOP.getRoleID()).getAsMention(), true);
        addField(event, games, Roles.DIABLO);
        addField(event, games, Roles.NEWWORLD);

        EmbedBuilder other = new EmbedBuilder();
        other.setTitle("Other");
        other.setFooter("Other", jda.getSelfUser().getAvatarUrl());
        addField(event, other, Roles.PLAYSTATION);
        addField(event, other, Roles.XBOX);
        addField(event, other, Roles.NINTENDO);
        addField(event, other, Roles.VR);
        addField(event, other, Roles.BOT);
        addField(event, other, Roles.SHISHA);
        addField(event, other, Roles.ANIME);
        addField(event, other, Roles.CODING);
        addField(event, other, Roles.STEAM);
        addField(event, other, Roles.EPIC);
        addField(event, other, Roles.REDDIT);
        addField(event, other, Roles.DISNEY);
        addField(event, other, Roles.PRIME);
        addField(event, other, Roles.NETFLIX);
        addField(event, other, Roles.STONKS);
        other.addField(Roles.ALCOHOL.getEmoteID(), guild.getRoleById(Roles.ALCOHOL.getRoleID()).getAsMention(), true);

        EmbedBuilder colors = new EmbedBuilder();
        colors.setTitle("Colors");
        colors.setFooter("Colors", jda.getSelfUser().getAvatarUrl());
        colors.addField(Roles.BROWN.getEmoteID(), guild.getRoleById(Roles.BROWN.getRoleID()).getAsMention(), true);
        colors.addField(Roles.GREEN.getEmoteID(), guild.getRoleById(Roles.GREEN.getRoleID()).getAsMention(), true);
        colors.addField(Roles.BLUE.getEmoteID(), guild.getRoleById(Roles.BLUE.getRoleID()).getAsMention(), true);
        colors.addField(Roles.PURPLE.getEmoteID(), guild.getRoleById(Roles.PURPLE.getRoleID()).getAsMention(), true);
        colors.addField(Roles.YELLOW.getEmoteID(), guild.getRoleById(Roles.YELLOW.getRoleID()).getAsMention(), true);
        colors.addField(Roles.ORANGE.getEmoteID(), guild.getRoleById(Roles.ORANGE.getRoleID()).getAsMention(), true);
        colors.addField(Roles.RED.getEmoteID(), guild.getRoleById(Roles.RED.getRoleID()).getAsMention(), true);

        channel.editMessageById(846850718462902272L, "@everyone\nIn this Channel you can give yourself roles which will be pinged when something relevant happens or someone wants to talk to you. Simply react with the corresponding emote and your role will be granted. If you want to remove a role simply remove your reaction.").queue();

        channel.editMessageById(846850719305695272L, games.build()
        ).queue(message -> {
            message.addReaction(Emotes.LOL).queue();
            message.addReaction(Emotes.MINECRAFT).queue();
            message.addReaction(Emotes.WARZONE).queue();
            message.addReaction(Emotes.APEX).queue();
            message.addReaction(Emotes.GMOD).queue();
            message.addReaction(Emotes.CIV).queue();
            message.addReaction(Emotes.ARK).queue();
            message.addReaction(Emotes.GTA).queue();
            message.addReaction(Emotes.HEARTHSTONE).queue();
            message.addReaction(Emotes.ROCKETLEAGUE).queue();
            message.addReaction(Emotes.AMONGUS).queue();
            message.addReaction(Emotes.VALORANT).queue();
            message.addReaction(Emotes.CSGO).queue();
            message.addReaction(Emotes.RAINBOW).queue();
            message.addReaction(Emotes.ROUNDS).queue();
            message.addReaction(Emotes.PUMMELPARTY).queue();
            message.addReaction(Emotes.SATISFACTORY).queue();
            message.addReaction(Emotes.FORTNITE).queue();
            message.addReaction("♟").queue();
            message.addReaction(Emotes.DIABLO).queue();
            message.addReaction(Emotes.NEWWORLD).queue();
        });
        channel.editMessageById(846850723395928144L, other.build()
        ).queue(message -> {
            message.addReaction(Emotes.PLAYSTATION).queue();
            message.addReaction(Emotes.BOT).queue();
            message.addReaction(Emotes.SWITCH).queue();
            message.addReaction(Emotes.XBOX).queue();
            message.addReaction(Emotes.VR).queue();
            message.addReaction(Emotes.SHISHA).queue();
            message.addReaction(Emotes.ANIME).queue();
            message.addReaction(Emotes.CODING).queue();
            message.addReaction(Emotes.STEAM).queue();
            message.addReaction(Emotes.EPIC).queue();
            message.addReaction(Emotes.REDDIT).queue();
            message.addReaction(Emotes.DISNEY).queue();
            message.addReaction(Emotes.PRIME).queue();
            message.addReaction(Emotes.NETFLIX).queue();
            message.addReaction(Emotes.STONKS).queue();
            message.addReaction("\uD83C\uDF7E").queue();
        });
        channel.editMessageById(846850724528914463L, colors.build()
        ).queue(message -> {
            message.addReaction("\uD83D\uDFE4").queue();
            message.addReaction("\uD83D\uDFE2").queue();
            message.addReaction("\uD83D\uDD35").queue();
            message.addReaction("\uD83D\uDFE3").queue();
            message.addReaction("\uD83D\uDFE1").queue();
            message.addReaction("\uD83D\uDFE0").queue();
            message.addReaction("\uD83D\uDD34").queue();
        });
    }

    private void addField(@NotNull ReadyEvent event, @NotNull EmbedBuilder embed, @NotNull Role role) {
        getGuildChannelJDA(event);

        embed.addField(Objects.requireNonNull(jda.getEmoteById(role.getEmoteIDLong())).getAsMention(), Objects.requireNonNull(guild.getRoleById(role.getRoleID())).getAsMention(), true);
    }
}
