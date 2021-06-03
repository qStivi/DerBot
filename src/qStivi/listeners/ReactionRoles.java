package qStivi.listeners;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import qStivi.Bot;
import qStivi.Emotes;

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
                case "LoL" -> addRoleToMember(event, 843108657079779359L);
                case "Minecraft" -> addRoleToMember(event, 755490059976966254L);
                case "AmongUs" -> addRoleToMember(event, 750014132220330016L);
                case "Warzone" -> addRoleToMember(event, 846009044610580500L);
                case "Apex" -> addRoleToMember(event, 846010998728425472L);
                case "Playstation" -> addRoleToMember(event, 846784335804760079L);
                case "Bot" -> addRoleToMember(event, 846784745073672252L);
                case "GMod" -> addRoleToMember(event, 846785053819666502L);
                case "Switch" -> addRoleToMember(event, 846785268694777896L);
                case "Civ" -> addRoleToMember(event, 846785388076335137L);
                case "Xbox" -> addRoleToMember(event, 846785641474818059L);
                case "ARK" -> addRoleToMember(event, 846785894525567007L);
                case "GTA5" -> addRoleToMember(event, 846786036750614560L);
                case "Hearthstone" -> addRoleToMember(event, 846786187653677077L);
                case "VR" -> addRoleToMember(event, 846786480381624410L);
                case "RocketLeague" -> addRoleToMember(event, 846786687484821545L);
                case "Shisha" -> addRoleToMember(event, 846808961894318110L);
                case "Valorant" -> addRoleToMember(event, 846815650260451429L);
                case "CSGO" -> addRoleToMember(event, 846819632161226803L);
                case "RainbowSixSiege" -> addRoleToMember(event, 846827756767281182L);
                case "Rounds" -> addRoleToMember(event, 846829319091126293L);
                case "Pummel" -> addRoleToMember(event, 846832373374517278L);
                case "Satisfactory" -> addRoleToMember(event, 846833665191051314L);
                case "Fortnite" -> addRoleToMember(event, 846836340729315379L);
                case "ayaya" -> addRoleToMember(event, 846838972093300786L);
                case "Diablo3" -> addRoleToMember(event, 846830616129175642L);
                case "Coding" -> addRoleToMember(event, 849930431902384128L);
                case "♟" -> addRoleToMember(event, 846817735798685696L);
                case "\uD83C\uDF7E" -> addRoleToMember(event, 846810390196256798L);
                case "\uD83D\uDFE4" -> addRoleToMember(event, 843120253566320672L);
                case "\uD83D\uDFE2" -> addRoleToMember(event, 843120087245389824L);
                case "\uD83D\uDD35" -> addRoleToMember(event, 843120035853107210L);
                case "\uD83D\uDFE3" -> addRoleToMember(event, 843119963270545439L);
                case "\uD83D\uDFE1" -> addRoleToMember(event, 843120339508658186L);
                case "\uD83D\uDFE0" -> addRoleToMember(event, 843120410190020628L);
                case "\uD83D\uDD34" -> addRoleToMember(event, 843120454067945483L);
                case "⚫" -> addRoleToMember(event, 843120566655647746L);
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
                case "LoL" -> removeRoleFromMember(event, 843108657079779359L);
                case "Minecraft" -> removeRoleFromMember(event, 755490059976966254L);
                case "AmongUs" -> removeRoleFromMember(event, 750014132220330016L);
                case "Warzone" -> removeRoleFromMember(event, 846009044610580500L);
                case "Apex" -> removeRoleFromMember(event, 846010998728425472L);
                case "Playstation" -> removeRoleFromMember(event, 846784335804760079L);
                case "Bot" -> removeRoleFromMember(event, 846784745073672252L);
                case "GMod" -> removeRoleFromMember(event, 846785053819666502L);
                case "Switch" -> removeRoleFromMember(event, 846785268694777896L);
                case "Civ" -> removeRoleFromMember(event, 846785388076335137L);
                case "Xbox" -> removeRoleFromMember(event, 846785641474818059L);
                case "ARK" -> removeRoleFromMember(event, 846785894525567007L);
                case "GTA5" -> removeRoleFromMember(event, 846786036750614560L);
                case "Hearthstone" -> removeRoleFromMember(event, 846786187653677077L);
                case "VR" -> removeRoleFromMember(event, 846786480381624410L);
                case "RocketLeague" -> removeRoleFromMember(event, 846786687484821545L);
                case "Shisha" -> removeRoleFromMember(event, 846808961894318110L);
                case "Valorant" -> removeRoleFromMember(event, 846815650260451429L);
                case "CSGO" -> removeRoleFromMember(event, 846819632161226803L);
                case "RainbowSixSiege" -> removeRoleFromMember(event, 846827756767281182L);
                case "Rounds" -> removeRoleFromMember(event, 846829319091126293L);
                case "Pummel" -> removeRoleFromMember(event, 846832373374517278L);
                case "Satisfactory" -> removeRoleFromMember(event, 846833665191051314L);
                case "Fortnite" -> removeRoleFromMember(event, 846836340729315379L);
                case "ayaya" -> removeRoleFromMember(event, 846838972093300786L);
                case "Diablo3" -> removeRoleFromMember(event, 846830616129175642L);
                case "Coding" -> removeRoleFromMember(event, 849930431902384128L);
                case "♟" -> removeRoleFromMember(event, 846817735798685696L);
                case "\uD83C\uDF7E" -> removeRoleFromMember(event, 846810390196256798L);
                case "\uD83D\uDFE4" -> removeRoleFromMember(event, 843120253566320672L);
                case "\uD83D\uDFE2" -> removeRoleFromMember(event, 843120087245389824L);
                case "\uD83D\uDD35" -> removeRoleFromMember(event, 843120035853107210L);
                case "\uD83D\uDFE3" -> removeRoleFromMember(event, 843119963270545439L);
                case "\uD83D\uDFE1" -> removeRoleFromMember(event, 843120339508658186L);
                case "\uD83D\uDFE0" -> removeRoleFromMember(event, 843120410190020628L);
                case "\uD83D\uDD34" -> removeRoleFromMember(event, 843120454067945483L);
                case "⚫" -> removeRoleFromMember(event, 843120566655647746L);
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
        games.addField(jda.getEmoteById(Emotes.getEmoteIDLong(Emotes.LOL)).getAsMention(), guild.getRolesByName("League of Legends", true).get(0).getAsMention(), true);
        games.addField(jda.getEmoteById(Emotes.getEmoteIDLong(Emotes.MINECRAFT)).getAsMention(), guild.getRolesByName("Minecraft", true).get(0).getAsMention(), true);
        games.addField(jda.getEmoteById(Emotes.getEmoteIDLong(Emotes.WARZONE)).getAsMention(), guild.getRolesByName("Warzone", true).get(0).getAsMention(), true);
        games.addField(jda.getEmoteById(Emotes.getEmoteIDLong(Emotes.APEX)).getAsMention(), guild.getRolesByName("Apex", true).get(0).getAsMention(), true);
        games.addField(jda.getEmoteById(Emotes.getEmoteIDLong(Emotes.GMOD)).getAsMention(), guild.getRolesByName("Garry's Mod", true).get(0).getAsMention(), true);
        games.addField(jda.getEmoteById(Emotes.getEmoteIDLong(Emotes.CIV)).getAsMention(), guild.getRolesByName("Civilization VI", true).get(0).getAsMention(), true);
        games.addField(jda.getEmoteById(Emotes.getEmoteIDLong(Emotes.ARK)).getAsMention(), guild.getRolesByName("ARK", true).get(0).getAsMention(), true);
        games.addField(jda.getEmoteById(Emotes.getEmoteIDLong(Emotes.GTA)).getAsMention(), guild.getRolesByName("GTA", true).get(0).getAsMention(), true);
        games.addField(jda.getEmoteById(Emotes.getEmoteIDLong(Emotes.HEARTHSTONE)).getAsMention(), guild.getRolesByName("Hearthstone", true).get(0).getAsMention(), true);
        games.addField(jda.getEmoteById(Emotes.getEmoteIDLong(Emotes.ROCKETLEAGUE)).getAsMention(), guild.getRolesByName("Rocket League", true).get(0).getAsMention(), true);
        games.addField(jda.getEmoteById(Emotes.getEmoteIDLong(Emotes.AMONGUS)).getAsMention(), guild.getRolesByName("Among Us", true).get(0).getAsMention(), true);
        games.addField(jda.getEmoteById(Emotes.getEmoteIDLong(Emotes.VALORANT)).getAsMention(), guild.getRolesByName("Valorant", true).get(0).getAsMention(), true);
        games.addField(jda.getEmoteById(Emotes.getEmoteIDLong(Emotes.CSGO)).getAsMention(), guild.getRolesByName("CSGO", true).get(0).getAsMention(), true);
        games.addField(jda.getEmoteById(Emotes.getEmoteIDLong(Emotes.RAINBOW)).getAsMention(), guild.getRolesByName("Rainbow", true).get(0).getAsMention(), true);
        games.addField(jda.getEmoteById(Emotes.getEmoteIDLong(Emotes.ROUNDS)).getAsMention(), guild.getRolesByName("Rounds", true).get(0).getAsMention(), true);
        games.addField(jda.getEmoteById(Emotes.getEmoteIDLong(Emotes.PUMMELPARTY)).getAsMention(), guild.getRolesByName("Pummel Party", true).get(0).getAsMention(), true);
        games.addField(jda.getEmoteById(Emotes.getEmoteIDLong(Emotes.SATISFACTORY)).getAsMention(), guild.getRolesByName("Satisfactory", true).get(0).getAsMention(), true);
        games.addField(jda.getEmoteById(Emotes.getEmoteIDLong(Emotes.FORTNITE)).getAsMention(), guild.getRolesByName("Fortnite", true).get(0).getAsMention(), true);
        games.addField(":chess_pawn:", guild.getRolesByName("Tabletop Simulator", true).get(0).getAsMention(), true);
        games.addField(jda.getEmoteById(Emotes.getEmoteIDLong(Emotes.DIABLO)).getAsMention(), guild.getRolesByName("Diablo III", true).get(0).getAsMention(), true);

        EmbedBuilder other = new EmbedBuilder();
        other.setTitle("Other");
        other.setFooter("Other", jda.getSelfUser().getAvatarUrl());
        other.addField(jda.getEmoteById(Emotes.getEmoteIDLong(Emotes.PLAYSTATION)).getAsMention(), guild.getRolesByName("Playstation", true).get(0).getAsMention(), true);
        other.addField(jda.getEmoteById(Emotes.getEmoteIDLong(Emotes.XBOX)).getAsMention(), guild.getRolesByName("XBOX", true).get(0).getAsMention(), true);
        other.addField(jda.getEmoteById(Emotes.getEmoteIDLong(Emotes.NINTENDO)).getAsMention(), guild.getRolesByName("Nintendo", true).get(0).getAsMention(), true);
        other.addField(jda.getEmoteById(Emotes.getEmoteIDLong(Emotes.VR)).getAsMention(), guild.getRolesByName("VR", true).get(0).getAsMention(), true);
        other.addField(jda.getEmoteById(Emotes.getEmoteIDLong(Emotes.BOT)).getAsMention(), guild.getRolesByName("Bot", true).get(0).getAsMention(), true);
        other.addField(jda.getEmoteById(Emotes.getEmoteIDLong(Emotes.SHISHA)).getAsMention(), guild.getRolesByName("Shisha", true).get(0).getAsMention(), true);
        other.addField(jda.getEmoteById(Emotes.getEmoteIDLong(Emotes.ANIME)).getAsMention(), guild.getRolesByName("Anime", true).get(0).getAsMention(), true);
        other.addField(jda.getEmoteById(Emotes.getEmoteIDLong(Emotes.CODING)).getAsMention(), guild.getRolesByName("Coding", true).get(0).getAsMention(), true);
        other.addField(":champagne:", guild.getRolesByName("Alkohol", true).get(0).getAsMention(), true);

        EmbedBuilder colors = new EmbedBuilder();
        colors.setTitle("Colors");
        colors.setFooter("Colors", jda.getSelfUser().getAvatarUrl());
        colors.addField(":brown_circle:", guild.getRolesByName("Brown", true).get(0).getAsMention(), true);
        colors.addField(":green_circle:", guild.getRolesByName("Green", true).get(0).getAsMention(), true);
        colors.addField(":blue_circle:", guild.getRolesByName("Blue", true).get(0).getAsMention(), true);
        colors.addField(":purple_circle:", guild.getRolesByName("Purple", true).get(0).getAsMention(), true);
        colors.addField(":yellow_circle:", guild.getRolesByName("Yellow", true).get(0).getAsMention(), true);
        colors.addField(":orange_circle:", guild.getRolesByName("Orange", true).get(0).getAsMention(), true);
        colors.addField(":red_circle:", guild.getRolesByName("Red", true).get(0).getAsMention(), true);

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
}
