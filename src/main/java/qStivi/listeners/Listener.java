package qStivi.listeners;

import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import qStivi.Bot;
import qStivi.db.DB;

import java.sql.SQLException;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import static org.slf4j.LoggerFactory.getLogger;
import static qStivi.Bot.DEV_CHANNEL_ID;

public class Listener extends ListenerAdapter {

    private static final Logger logger = getLogger(Listener.class);
    public final BlockingQueue<Command> queue = new LinkedBlockingQueue<>();
    Thread timer;

    @Override
    public void onReady(@NotNull ReadyEvent event) {
        logger.info("Ready!");
    }

    public Listener() {
        timer = new Thread()


        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {

            }
        }, 1000, 1000);



    }

    @Override
    public void onGuildVoiceJoin(@NotNull GuildVoiceJoinEvent event) {
        if (Bot.DEV_MODE && !event.getChannelJoined().getId().equals(Bot.DEV_VOICE_CHANNEL_ID)) return;
        if (event.getMember().getUser().isBot()) return;

    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {

        var channelID = event.getChannel().getIdLong();
        var channel = event.getChannel();
        var parent = channel.getParent();
        var categoryID = parent == null ? 0 : parent.getIdLong();
        var author = event.getAuthor();

        if (author.isBot()) return;
        if (event.isWebhookMessage()) return;
        if (Bot.DEV_MODE && channelID != DEV_CHANNEL_ID) {
            return;
        } else if (!Bot.DEV_MODE && (channelID == DEV_CHANNEL_ID || categoryID != 833734651070775338L)) {
            return;
        }

        event.getGuild().getTextChannelById(Bot.CHANNEL_ID).getManager().setName(String.valueOf(event.getGuild().getMemberCount())).queue();

        String messageRaw = event.getMessage().getContentRaw();

        /*
        Reactions
         */
        if (messageRaw.toLowerCase().startsWith("ree")) {
            String[] words = messageRaw.split("\\s+");
            String ree = words[0];
            String ees = ree.substring(1);
            channel.sendMessage(ree + ees + ees).queue();
        }

        if (messageRaw.toLowerCase().startsWith("hmm")) {
            event.getMessage().addReaction("U+1F914").queue();
        }
    }

    @Override
    public void onGuildMessageReactionAdd(@NotNull GuildMessageReactionAddEvent event) {

        var channelID = event.getChannel().getIdLong();
        var channel = event.getChannel();
        var parent = channel.getParent();
        var categoryID = parent == null ? 0 : parent.getIdLong();
        var author = event.getUser();

        if (author.isBot()) return;
        if (Bot.DEV_MODE && channelID != DEV_CHANNEL_ID) {
            return;
        } else if (!Bot.DEV_MODE && (channelID == DEV_CHANNEL_ID || categoryID != 833734651070775338L)) {
            return;
        }

        try {

            var db = new DB();
            var id = author.getIdLong();

            db.setLastReaction(new Date().getTime(), id);
            db.incrementXPReaction(5, id);
            db.incrementXP(5, id);

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }
}
