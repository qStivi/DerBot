package qStivi.listeners;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import qStivi.Bot;
import qStivi.Emotes;
import qStivi.Roles;

import java.util.Objects;

import static org.slf4j.LoggerFactory.getLogger;

// TODO Make everything more readable and try to fix warnings
public class ReactionRoles extends ListenerAdapter {
    private static final Logger logger = getLogger(ReactionRoles.class);

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
                case "Fortnite" -> addRoleToMember(event, Roles.FORTNIGHT);
                case "ayaya" -> addRoleToMember(event, Roles.ANIME);
                case "Diablo3" -> addRoleToMember(event, Roles.DIABLO);
                case "Coding" -> addRoleToMember(event, Roles.CODING);
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

    private void addRoleToMember(GuildMessageReactionAddEvent event, long roleId) {
        event.getGuild().addRoleToMember(event.getMember(), Objects.requireNonNull(event.getGuild().getRoleById(roleId))).queue();
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
                case "Fortnite" -> removeRoleFromMember(event, Roles.FORTNIGHT);
                case "ayaya" -> removeRoleFromMember(event, Roles.ANIME);
                case "Diablo3" -> removeRoleFromMember(event, Roles.DIABLO);
                case "Coding" -> removeRoleFromMember(event, Roles.CODING);
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

    private void removeRoleFromMember(GuildMessageReactionRemoveEvent event, long roleId) {
        event.getGuild().removeRoleFromMember(Objects.requireNonNull(event.getMember()), Objects.requireNonNull(event.getGuild().getRoleById(roleId))).queue();
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public void onReady(@NotNull ReadyEvent event) {

        if (Bot.DEV_MODE) return;

        var jda = event.getJDA();

        var guild = jda.getGuildById(703363806356701295L);
        if (guild == null) {
            logger.error("Guild is null!");
            return;
        }

        var channel = jda.getTextChannelById(843093823366365184L);

        EmbedBuilder games = new EmbedBuilder();
        games.setTitle("Video Games");
        games.setFooter("Video Games", jda.getSelfUser().getAvatarUrl());
        addField(event, games, Emotes.LOL, Roles.LOL);
        addField(event, games, Emotes.MINECRAFT, Roles.MINECRAFT);
        addField(event, games, Emotes.WARZONE, Roles.WARZONE);
        addField(event, games, Emotes.APEX, Roles.APEX);
        addField(event, games, Emotes.GMOD, Roles.GMOD);
        addField(event, games, Emotes.CIV, Roles.CIV);
        addField(event, games, Emotes.ARK, Roles.ARK);
        addField(event, games, Emotes.GTA, Roles.GTA);
        addField(event, games, Emotes.HEARTHSTONE, Roles.HEARTHSTONE);
        addField(event, games, Emotes.ROCKETLEAGUE, Roles.ROCKETLEAGUE);
        addField(event, games, Emotes.AMONGUS, Roles.AMONGUS);
        addField(event, games, Emotes.VALORANT, Roles.VALORANT);
        addField(event, games, Emotes.CSGO, Roles.CSGO);
        addField(event, games, Emotes.RAINBOW, Roles.RAINBOW);
        addField(event, games, Emotes.ROUNDS, Roles.ROUNDS);
        addField(event, games, Emotes.PUMMELPARTY, Roles.PUMMELPARTY);
        addField(event, games, Emotes.SATISFACTORY, Roles.SATISFACTORY);
        addField(event, games, Emotes.FORTNITE, Roles.FORTNIGHT);
        games.addField(":chess_pawn:", guild.getRoleById(Roles.TABLETOP).getAsMention(), true);
        addField(event, games, Emotes.DIABLO, Roles.DIABLO);

        EmbedBuilder other = new EmbedBuilder();
        other.setTitle("Other");
        other.setFooter("Other", jda.getSelfUser().getAvatarUrl());
        addField(event, other, Emotes.PLAYSTATION, Roles.PLAYSTATION);
        addField(event, other, Emotes.XBOX, Roles.XBOX);
        addField(event, other, Emotes.NINTENDO, Roles.NINTENDO);
        addField(event, other, Emotes.VR, Roles.VR);
        addField(event, other, Emotes.BOT, Roles.BOT);
        addField(event, other, Emotes.SHISHA, Roles.SHISHA);
        addField(event, other, Emotes.ANIME, Roles.ANIME);
        addField(event, other, Emotes.CODING, Roles.CODING);
        other.addField(":champagne:", guild.getRoleById(Roles.ALCOHOL).getAsMention(), true);

        EmbedBuilder colors = new EmbedBuilder();
        colors.setTitle("Colors");
        colors.setFooter("Colors", jda.getSelfUser().getAvatarUrl());
        colors.addField(":brown_circle:", guild.getRoleById(Roles.BROWN).getAsMention(), true);
        colors.addField(":green_circle:", guild.getRoleById(Roles.GREEN).getAsMention(), true);
        colors.addField(":blue_circle:", guild.getRoleById(Roles.BLUE).getAsMention(), true);
        colors.addField(":purple_circle:", guild.getRoleById(Roles.PURPLE).getAsMention(), true);
        colors.addField(":yellow_circle:", guild.getRoleById(Roles.YELLOW).getAsMention(), true);
        colors.addField(":orange_circle:", guild.getRoleById(Roles.ORANGE).getAsMention(), true);
        colors.addField(":red_circle:", guild.getRoleById(Roles.RED).getAsMention(), true);

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
        });
        channel.editMessageById(846850723395928144L, other.build()
        ).queue(message -> {
            message.addReaction(Emotes.PLAYSTATION).queue();
            message.addReaction(Emotes.BOT).queue();
            message.addReaction(Emotes.NINTENDO).queue();
            message.addReaction(Emotes.XBOX).queue();
            message.addReaction(Emotes.VR).queue();
            message.addReaction(Emotes.SHISHA).queue();
            message.addReaction(Emotes.ANIME).queue();
            message.addReaction(Emotes.CODING).queue();
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

    private void addField(ReadyEvent event, EmbedBuilder embed, String emoteId, long roleId) {
        var jda = event.getJDA();
        embed.addField(Objects.requireNonNull(jda.getEmoteById(Emotes.getEmoteIDLong(emoteId))).getAsMention(), Objects.requireNonNull(Objects.requireNonNull(jda.getGuildById(703363806356701295L)).getRoleById(roleId)).getAsMention(), true);
    }
}
