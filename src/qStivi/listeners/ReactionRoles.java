package qStivi.listeners;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import qStivi.Bot;
import qStivi.Emotes;

@SuppressWarnings("ALL")
public class ReactionRoles extends ListenerAdapter {

    @Override
    public void onGuildMessageReactionAdd(@NotNull GuildMessageReactionAddEvent event) {

        if (Bot.DEV_MODE) return;

        if (event.getChannel().getIdLong() == 843093823366365184L) {
            var emote = event.getReactionEmote().getName();
            var id = event.getUserIdLong();
            switch (emote) {
                case "LoL" -> event.getGuild().addRoleToMember(id, event.getGuild().getRoleById(843108657079779359L)).queue();
                case "Minecraft" -> event.getGuild().addRoleToMember(id, event.getGuild().getRoleById(755490059976966254L)).queue();
                case "AmongUs" -> event.getGuild().addRoleToMember(id, event.getGuild().getRoleById(750014132220330016L)).queue();
                case "Warzone" -> event.getGuild().addRoleToMember(id, event.getGuild().getRoleById(846009044610580500L)).queue();
                case "Apex" -> event.getGuild().addRoleToMember(id, event.getGuild().getRoleById(846010998728425472L)).queue();
                case "Playstation" -> event.getGuild().addRoleToMember(id, event.getGuild().getRoleById(846784335804760079L)).queue();
                case "Bot" -> event.getGuild().addRoleToMember(id, event.getGuild().getRoleById(846784745073672252L)).queue();
                case "GMod" -> event.getGuild().addRoleToMember(id, event.getGuild().getRoleById(846785053819666502L)).queue();
                case "Switch" -> event.getGuild().addRoleToMember(id, event.getGuild().getRoleById(846785268694777896L)).queue();
                case "Civ" -> event.getGuild().addRoleToMember(id, event.getGuild().getRoleById(846785388076335137L)).queue();
                case "Xbox" -> event.getGuild().addRoleToMember(id, event.getGuild().getRoleById(846785641474818059L)).queue();
                case "ARK" -> event.getGuild().addRoleToMember(id, event.getGuild().getRoleById(846785894525567007L)).queue();
                case "GTA5" -> event.getGuild().addRoleToMember(id, event.getGuild().getRoleById(846786036750614560L)).queue();
                case "Hearthstone" -> event.getGuild().addRoleToMember(id, event.getGuild().getRoleById(846786187653677077L)).queue();
                case "VR" -> event.getGuild().addRoleToMember(id, event.getGuild().getRoleById(846786480381624410L)).queue();
                case "RocketLeague" -> event.getGuild().addRoleToMember(id, event.getGuild().getRoleById(846786687484821545L)).queue();
                case "Shisha" -> event.getGuild().addRoleToMember(id, event.getGuild().getRoleById(846808961894318110L)).queue();
                case "Valorant" -> event.getGuild().addRoleToMember(id, event.getGuild().getRoleById(846815650260451429L)).queue();
                case "CSGO" -> event.getGuild().addRoleToMember(id, event.getGuild().getRoleById(846819632161226803L)).queue();
                case "RainbowSixSiege" -> event.getGuild().addRoleToMember(id, event.getGuild().getRoleById(846827756767281182L)).queue();
                case "Rounds" -> event.getGuild().addRoleToMember(id, event.getGuild().getRoleById(846829319091126293L)).queue();
                case "Pummel" -> event.getGuild().addRoleToMember(id, event.getGuild().getRoleById(846832373374517278L)).queue();
                case "Satisfactory" -> event.getGuild().addRoleToMember(id, event.getGuild().getRoleById(846833665191051314L)).queue();
                case "Fortnite" -> event.getGuild().addRoleToMember(id, event.getGuild().getRoleById(846836340729315379L)).queue();
                case "ayaya" -> event.getGuild().addRoleToMember(id, event.getGuild().getRoleById(846838972093300786L)).queue();
                case "Diablo3" -> event.getGuild().addRoleToMember(id, event.getGuild().getRoleById(846830616129175642L)).queue();
                case "♟" -> event.getGuild().addRoleToMember(id, event.getGuild().getRoleById(846817735798685696L)).queue();
                case "\uD83C\uDF7E" -> event.getGuild().addRoleToMember(id, event.getGuild().getRoleById(846810390196256798L)).queue();
                case "\uD83D\uDFE4" -> event.getGuild().addRoleToMember(id, event.getGuild().getRoleById(843120253566320672L)).queue();
                case "\uD83D\uDFE2" -> event.getGuild().addRoleToMember(id, event.getGuild().getRoleById(843120087245389824L)).queue();
                case "\uD83D\uDD35" -> event.getGuild().addRoleToMember(id, event.getGuild().getRoleById(843120035853107210L)).queue();
                case "\uD83D\uDFE3" -> event.getGuild().addRoleToMember(id, event.getGuild().getRoleById(843119963270545439L)).queue();
                case "\uD83D\uDFE1" -> event.getGuild().addRoleToMember(id, event.getGuild().getRoleById(843120339508658186L)).queue();
                case "\uD83D\uDFE0" -> event.getGuild().addRoleToMember(id, event.getGuild().getRoleById(843120410190020628L)).queue();
                case "\uD83D\uDD34" -> event.getGuild().addRoleToMember(id, event.getGuild().getRoleById(843120454067945483L)).queue();
                case "⚫" -> event.getGuild().addRoleToMember(id, event.getGuild().getRoleById(843120566655647746L)).queue();
            }
        }
    }

    @Override
    public void onGuildMessageReactionRemove(@NotNull GuildMessageReactionRemoveEvent event) {

        if (Bot.DEV_MODE) return;

        if (event.getChannel().getIdLong() == 843093823366365184L) {
            var emote = event.getReactionEmote().getName();
            switch (emote) {
                case "LoL" -> event.getGuild().removeRoleFromMember(event.getUserIdLong(), event.getGuild().getRoleById(843108657079779359L)).queue();
                case "Minecraft" -> event.getGuild().removeRoleFromMember(event.getUserIdLong(), event.getGuild().getRoleById(755490059976966254L)).queue();
                case "AmongUs" -> event.getGuild().removeRoleFromMember(event.getUserIdLong(), event.getGuild().getRoleById(750014132220330016L)).queue();
                case "Warzone" -> event.getGuild().removeRoleFromMember(event.getUserIdLong(), event.getGuild().getRoleById(846009044610580500L)).queue();
                case "Apex" -> event.getGuild().removeRoleFromMember(event.getUserIdLong(), event.getGuild().getRoleById(846010998728425472L)).queue();
                case "Playstation" -> event.getGuild().removeRoleFromMember(event.getUserIdLong(), event.getGuild().getRoleById(846784335804760079L)).queue();
                case "Bot" -> event.getGuild().removeRoleFromMember(event.getUserIdLong(), event.getGuild().getRoleById(846784745073672252L)).queue();
                case "GMod" -> event.getGuild().removeRoleFromMember(event.getUserIdLong(), event.getGuild().getRoleById(846785053819666502L)).queue();
                case "Switch" -> event.getGuild().removeRoleFromMember(event.getUserIdLong(), event.getGuild().getRoleById(846785268694777896L)).queue();
                case "Civ" -> event.getGuild().removeRoleFromMember(event.getUserIdLong(), event.getGuild().getRoleById(846785388076335137L)).queue();
                case "Xbox" -> event.getGuild().removeRoleFromMember(event.getUserIdLong(), event.getGuild().getRoleById(846785641474818059L)).queue();
                case "ARK" -> event.getGuild().removeRoleFromMember(event.getUserIdLong(), event.getGuild().getRoleById(846785894525567007L)).queue();
                case "GTA5" -> event.getGuild().removeRoleFromMember(event.getUserIdLong(), event.getGuild().getRoleById(846786036750614560L)).queue();
                case "Hearthstone" -> event.getGuild().removeRoleFromMember(event.getUserIdLong(), event.getGuild().getRoleById(846786187653677077L)).queue();
                case "VR" -> event.getGuild().removeRoleFromMember(event.getUserIdLong(), event.getGuild().getRoleById(846786480381624410L)).queue();
                case "RocketLeague" -> event.getGuild().removeRoleFromMember(event.getUserIdLong(), event.getGuild().getRoleById(846786687484821545L)).queue();
                case "Shisha" -> event.getGuild().removeRoleFromMember(event.getUserIdLong(), event.getGuild().getRoleById(846808961894318110L)).queue();
                case "Valorant" -> event.getGuild().removeRoleFromMember(event.getUserIdLong(), event.getGuild().getRoleById(846815650260451429L)).queue();
                case "CSGO" -> event.getGuild().removeRoleFromMember(event.getUserIdLong(), event.getGuild().getRoleById(846819632161226803L)).queue();
                case "RainbowSixSiege" -> event.getGuild().removeRoleFromMember(event.getUserIdLong(), event.getGuild().getRoleById(846827756767281182L)).queue();
                case "Rounds" -> event.getGuild().removeRoleFromMember(event.getUserIdLong(), event.getGuild().getRoleById(846829319091126293L)).queue();
                case "Pummel" -> event.getGuild().removeRoleFromMember(event.getUserIdLong(), event.getGuild().getRoleById(846832373374517278L)).queue();
                case "Satisfactory" -> event.getGuild().removeRoleFromMember(event.getUserIdLong(), event.getGuild().getRoleById(846833665191051314L)).queue();
                case "Fortnite" -> event.getGuild().removeRoleFromMember(event.getUserIdLong(), event.getGuild().getRoleById(846836340729315379L)).queue();
                case "ayaya" -> event.getGuild().removeRoleFromMember(event.getUserIdLong(), event.getGuild().getRoleById(846838972093300786L)).queue();
                case "Diablo3" -> event.getGuild().removeRoleFromMember(event.getUserIdLong(), event.getGuild().getRoleById(846830616129175642L)).queue();
                case "♟" -> event.getGuild().removeRoleFromMember(event.getUserIdLong(), event.getGuild().getRoleById(846817735798685696L)).queue();
                case "\uD83C\uDF7E" -> event.getGuild().removeRoleFromMember(event.getUserIdLong(), event.getGuild().getRoleById(846810390196256798L)).queue();
                case "\uD83D\uDFE4" -> event.getGuild().removeRoleFromMember(event.getUserIdLong(), event.getGuild().getRoleById(843120253566320672L)).queue();
                case "\uD83D\uDFE2" -> event.getGuild().removeRoleFromMember(event.getUserIdLong(), event.getGuild().getRoleById(843120087245389824L)).queue();
                case "\uD83D\uDD35" -> event.getGuild().removeRoleFromMember(event.getUserIdLong(), event.getGuild().getRoleById(843120035853107210L)).queue();
                case "\uD83D\uDFE3" -> event.getGuild().removeRoleFromMember(event.getUserIdLong(), event.getGuild().getRoleById(843119963270545439L)).queue();
                case "\uD83D\uDFE1" -> event.getGuild().removeRoleFromMember(event.getUserIdLong(), event.getGuild().getRoleById(843120339508658186L)).queue();
                case "\uD83D\uDFE0" -> event.getGuild().removeRoleFromMember(event.getUserIdLong(), event.getGuild().getRoleById(843120410190020628L)).queue();
                case "\uD83D\uDD34" -> event.getGuild().removeRoleFromMember(event.getUserIdLong(), event.getGuild().getRoleById(843120454067945483L)).queue();
                case "⚫" -> event.getGuild().removeRoleFromMember(event.getUserIdLong(), event.getGuild().getRoleById(843120566655647746L)).queue();
            }
        }
    }

    @Override
    public void onReady(@NotNull ReadyEvent event) {

        if (Bot.DEV_MODE) return;

        var jda = event.getJDA();

        EmbedBuilder games = new EmbedBuilder();
        games.setTitle("Video Games");
        games.setFooter("Video Games", jda.getSelfUser().getAvatarUrl());
        games.addField(jda.getEmoteById(Emotes.getEmoteIDLong(Emotes.LOL)).getAsMention(), jda.getGuildById(Bot.GUILD_ID).getRolesByName("League of Legends", true).get(0).getAsMention(), true);
        games.addField(jda.getEmoteById(Emotes.getEmoteIDLong(Emotes.MINECRAFT)).getAsMention(), jda.getGuildById(Bot.GUILD_ID).getRolesByName("Minecraft", true).get(0).getAsMention(), true);
        games.addField(jda.getEmoteById(Emotes.getEmoteIDLong(Emotes.WARZONE)).getAsMention(), jda.getGuildById(Bot.GUILD_ID).getRolesByName("Warzone", true).get(0).getAsMention(), true);
        games.addField(jda.getEmoteById(Emotes.getEmoteIDLong(Emotes.APEX)).getAsMention(), jda.getGuildById(Bot.GUILD_ID).getRolesByName("Apex", true).get(0).getAsMention(), true);
        games.addField(jda.getEmoteById(Emotes.getEmoteIDLong(Emotes.GMOD)).getAsMention(), jda.getGuildById(Bot.GUILD_ID).getRolesByName("Garry's Mod", true).get(0).getAsMention(), true);
        games.addField(jda.getEmoteById(Emotes.getEmoteIDLong(Emotes.CIV)).getAsMention(), jda.getGuildById(Bot.GUILD_ID).getRolesByName("Civilization VI", true).get(0).getAsMention(), true);
        games.addField(jda.getEmoteById(Emotes.getEmoteIDLong(Emotes.ARK)).getAsMention(), jda.getGuildById(Bot.GUILD_ID).getRolesByName("ARK", true).get(0).getAsMention(), true);
        games.addField(jda.getEmoteById(Emotes.getEmoteIDLong(Emotes.GTA)).getAsMention(), jda.getGuildById(Bot.GUILD_ID).getRolesByName("GTA", true).get(0).getAsMention(), true);
        games.addField(jda.getEmoteById(Emotes.getEmoteIDLong(Emotes.HEARTHSTONE)).getAsMention(), jda.getGuildById(Bot.GUILD_ID).getRolesByName("Hearthstone", true).get(0).getAsMention(), true);
        games.addField(jda.getEmoteById(Emotes.getEmoteIDLong(Emotes.ROCKETLEAGUE)).getAsMention(), jda.getGuildById(Bot.GUILD_ID).getRolesByName("Rocket League", true).get(0).getAsMention(), true);
        games.addField(jda.getEmoteById(Emotes.getEmoteIDLong(Emotes.AMONGUS)).getAsMention(), jda.getGuildById(Bot.GUILD_ID).getRolesByName("Among Us", true).get(0).getAsMention(), true);
        games.addField(jda.getEmoteById(Emotes.getEmoteIDLong(Emotes.VALORANT)).getAsMention(), jda.getGuildById(Bot.GUILD_ID).getRolesByName("Valorant", true).get(0).getAsMention(), true);
        games.addField(jda.getEmoteById(Emotes.getEmoteIDLong(Emotes.CSGO)).getAsMention(), jda.getGuildById(Bot.GUILD_ID).getRolesByName("CSGO", true).get(0).getAsMention(), true);
        games.addField(jda.getEmoteById(Emotes.getEmoteIDLong(Emotes.RAINBOW)).getAsMention(), jda.getGuildById(Bot.GUILD_ID).getRolesByName("Rainbow", true).get(0).getAsMention(), true);
        games.addField(jda.getEmoteById(Emotes.getEmoteIDLong(Emotes.ROUNDS)).getAsMention(), jda.getGuildById(Bot.GUILD_ID).getRolesByName("Rounds", true).get(0).getAsMention(), true);
        games.addField(jda.getEmoteById(Emotes.getEmoteIDLong(Emotes.PUMMELPARTY)).getAsMention(), jda.getGuildById(Bot.GUILD_ID).getRolesByName("Pummel Party", true).get(0).getAsMention(), true);
        games.addField(jda.getEmoteById(Emotes.getEmoteIDLong(Emotes.SATISFACTORY)).getAsMention(), jda.getGuildById(Bot.GUILD_ID).getRolesByName("Satisfactory", true).get(0).getAsMention(), true);
        games.addField(jda.getEmoteById(Emotes.getEmoteIDLong(Emotes.FORTNITE)).getAsMention(), jda.getGuildById(Bot.GUILD_ID).getRolesByName("Fortnite", true).get(0).getAsMention(), true);
        games.addField(":chess_pawn:", jda.getGuildById(Bot.GUILD_ID).getRolesByName("Tabletop Simulator", true).get(0).getAsMention(), true);
        games.addField(jda.getEmoteById(Emotes.getEmoteIDLong(Emotes.DIABLO)).getAsMention(), jda.getGuildById(Bot.GUILD_ID).getRolesByName("Diablo III", true).get(0).getAsMention(), true);

        EmbedBuilder other = new EmbedBuilder();
        other.setTitle("Other");
        other.setFooter("Other", jda.getSelfUser().getAvatarUrl());
        other.addField(jda.getEmoteById(Emotes.getEmoteIDLong(Emotes.PLAYSTATION)).getAsMention(), jda.getGuildById(Bot.GUILD_ID).getRolesByName("Playstation", true).get(0).getAsMention(), true);
        other.addField(jda.getEmoteById(Emotes.getEmoteIDLong(Emotes.XBOX)).getAsMention(), jda.getGuildById(Bot.GUILD_ID).getRolesByName("XBOX", true).get(0).getAsMention(), true);
        other.addField(jda.getEmoteById(Emotes.getEmoteIDLong(Emotes.NINTENDO)).getAsMention(), jda.getGuildById(Bot.GUILD_ID).getRolesByName("Nintendo", true).get(0).getAsMention(), true);
        other.addField(jda.getEmoteById(Emotes.getEmoteIDLong(Emotes.VR)).getAsMention(), jda.getGuildById(Bot.GUILD_ID).getRolesByName("VR", true).get(0).getAsMention(), true);
        other.addField(jda.getEmoteById(Emotes.getEmoteIDLong(Emotes.BOT)).getAsMention(), jda.getGuildById(Bot.GUILD_ID).getRolesByName("Bot", true).get(0).getAsMention(), true);
        other.addField(jda.getEmoteById(Emotes.getEmoteIDLong(Emotes.SHISHA)).getAsMention(), jda.getGuildById(Bot.GUILD_ID).getRolesByName("Shisha", true).get(0).getAsMention(), true);
        other.addField(jda.getEmoteById(Emotes.getEmoteIDLong(Emotes.ANIME)).getAsMention(), jda.getGuildById(Bot.GUILD_ID).getRolesByName("Anime", true).get(0).getAsMention(), true);
        other.addField(":champagne:", jda.getGuildById(Bot.GUILD_ID).getRolesByName("Alkohol", true).get(0).getAsMention(), true);

        EmbedBuilder colors = new EmbedBuilder();
        colors.setTitle("Colors");
        colors.setFooter("Colors", jda.getSelfUser().getAvatarUrl());
        colors.addField(":brown_circle:", jda.getGuildById(Bot.GUILD_ID).getRolesByName("Brown", true).get(0).getAsMention(), true);
        colors.addField(":green_circle:", jda.getGuildById(Bot.GUILD_ID).getRolesByName("Green", true).get(0).getAsMention(), true);
        colors.addField(":blue_circle:", jda.getGuildById(Bot.GUILD_ID).getRolesByName("Blue", true).get(0).getAsMention(), true);
        colors.addField(":purple_circle:", jda.getGuildById(Bot.GUILD_ID).getRolesByName("Purple", true).get(0).getAsMention(), true);
        colors.addField(":yellow_circle:", jda.getGuildById(Bot.GUILD_ID).getRolesByName("Yellow", true).get(0).getAsMention(), true);
        colors.addField(":orange_circle:", jda.getGuildById(Bot.GUILD_ID).getRolesByName("Orange", true).get(0).getAsMention(), true);
        colors.addField(":red_circle:", jda.getGuildById(Bot.GUILD_ID).getRolesByName("Red", true).get(0).getAsMention(), true);

        jda.getTextChannelById(843093823366365184L).editMessageById(846850718462902272L, "@everyone\nIn this Channel you can give yourself roles which will be pinged when something relevant happens or someone wants to talk to you. Simply react with the corresponding emote and your role will be granted. If you want to remove a role simply remove your reaction.").queue();

        jda.getTextChannelById(843093823366365184L).editMessageById(846850719305695272L, games.build()
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
        jda.getTextChannelById(843093823366365184L).editMessageById(846850723395928144L, other.build()
        ).queue(message -> {
            message.addReaction(Emotes.PLAYSTATION).queue();
            message.addReaction(Emotes.BOT).queue();
            message.addReaction(Emotes.NINTENDO).queue();
            message.addReaction(Emotes.XBOX).queue();
            message.addReaction(Emotes.VR).queue();
            message.addReaction(Emotes.SHISHA).queue();
            message.addReaction(Emotes.ANIME).queue();
            message.addReaction("\uD83C\uDF7E").queue();
        });
        jda.getTextChannelById(843093823366365184L).editMessageById(846850724528914463L, colors.build()
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
