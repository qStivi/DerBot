package qStivi.listeners;

import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import qStivi.Bot;

import static org.slf4j.LoggerFactory.getLogger;

public class ReactionRoles extends ListenerAdapter {
    private static final Logger logger = getLogger(ReactionRoles.class);

    @Override
    public void onGuildMessageReactionAdd(@NotNull GuildMessageReactionAddEvent event) {
        if (event.getMessageIdLong() == 845661569085079603L){
            var emote = event.getReactionEmote().getName();
            logger.info(emote);
            switch (emote){
                case "LoL":
                    event.getGuild().addRoleToMember(event.getUserIdLong(), event.getGuild().getRoleById(843108657079779359L)).queue();
                    break;
                case "Minecraft":
                    event.getGuild().addRoleToMember(event.getUserIdLong(), event.getGuild().getRoleById(755490059976966254L)).queue();
                    break;
                case "AmongUs":
                    event.getGuild().addRoleToMember(event.getUserIdLong(), event.getGuild().getRoleById(750014132220330016L)).queue();
                    break;
                case "\uD83D\uDFE4":
                    event.getGuild().addRoleToMember(event.getUserIdLong(), event.getGuild().getRoleById(843120253566320672L)).queue();
                    break;
                case "\uD83D\uDFE2":
                    event.getGuild().addRoleToMember(event.getUserIdLong(), event.getGuild().getRoleById(843120087245389824L)).queue();
                    break;
                case "\uD83D\uDD35":
                    event.getGuild().addRoleToMember(event.getUserIdLong(), event.getGuild().getRoleById(843120035853107210L)).queue();
                    break;
                case "\uD83D\uDFE3":
                    event.getGuild().addRoleToMember(event.getUserIdLong(), event.getGuild().getRoleById(843119963270545439L)).queue();
                    break;
                case "\uD83D\uDFE1":
                    event.getGuild().addRoleToMember(event.getUserIdLong(), event.getGuild().getRoleById(843120339508658186L)).queue();
                    break;
                case "\uD83D\uDFE0":
                    event.getGuild().addRoleToMember(event.getUserIdLong(), event.getGuild().getRoleById(843120410190020628L)).queue();
                    break;
                case "\uD83D\uDD34":
                    event.getGuild().addRoleToMember(event.getUserIdLong(), event.getGuild().getRoleById(843120454067945483L)).queue();
                    break;
                case "⚫":
                    event.getGuild().addRoleToMember(event.getUserIdLong(), event.getGuild().getRoleById(843120566655647746L)).queue();
                    break;
                case "⚪":
                    event.getGuild().addRoleToMember(event.getUserIdLong(), event.getGuild().getRoleById(843120664532877344L)).queue();
                    break;
            }
        }
    }

    @Override
    public void onGuildMessageReactionRemove(@NotNull GuildMessageReactionRemoveEvent event) {
        if (event.getMessageIdLong() == 845661569085079603L){
            var emote = event.getReactionEmote().getName();
            switch (emote){
                case "LoL":
                    event.getGuild().removeRoleFromMember(event.getUserIdLong(), event.getGuild().getRoleById(843108657079779359L)).queue();
                    break;
                case "Minecraft":
                    event.getGuild().removeRoleFromMember(event.getUserIdLong(), event.getGuild().getRoleById(755490059976966254L)).queue();
                    break;
                case "AmongUs":
                    event.getGuild().removeRoleFromMember(event.getUserIdLong(), event.getGuild().getRoleById(750014132220330016L)).queue();
                    break;
                case "\uD83D\uDFE4":
                    event.getGuild().removeRoleFromMember(event.getUserIdLong(), event.getGuild().getRoleById(843120253566320672L)).queue();
                    break;
                case "\uD83D\uDFE2":
                    event.getGuild().removeRoleFromMember(event.getUserIdLong(), event.getGuild().getRoleById(843120087245389824L)).queue();
                    break;
                case "\uD83D\uDD35":
                    event.getGuild().removeRoleFromMember(event.getUserIdLong(), event.getGuild().getRoleById(843120035853107210L)).queue();
                    break;
                case "\uD83D\uDFE3":
                    event.getGuild().removeRoleFromMember(event.getUserIdLong(), event.getGuild().getRoleById(843119963270545439L)).queue();
                    break;
                case "\uD83D\uDFE1":
                    event.getGuild().removeRoleFromMember(event.getUserIdLong(), event.getGuild().getRoleById(843120339508658186L)).queue();
                    break;
                case "\uD83D\uDFE0":
                    event.getGuild().removeRoleFromMember(event.getUserIdLong(), event.getGuild().getRoleById(843120410190020628L)).queue();
                    break;
                case "\uD83D\uDD34":
                    event.getGuild().removeRoleFromMember(event.getUserIdLong(), event.getGuild().getRoleById(843120454067945483L)).queue();
                    break;
                case "⚫":
                    event.getGuild().removeRoleFromMember(event.getUserIdLong(), event.getGuild().getRoleById(843120566655647746L)).queue();
                    break;
                case "⚪":
                    event.getGuild().removeRoleFromMember(event.getUserIdLong(), event.getGuild().getRoleById(843120664532877344L)).queue();
                    break;
            }
        }
    }
}
