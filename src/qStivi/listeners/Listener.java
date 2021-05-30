package qStivi.listeners;

import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import qStivi.Bot;
import qStivi.commands.rpg.SkillsCommand;
import qStivi.DB;

import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

import static org.slf4j.LoggerFactory.getLogger;
import static qStivi.Bot.CHANNEL_ID;
import static qStivi.Bot.DEV_CHANNEL_ID;

public class Listener extends ListenerAdapter {

    private static final Logger logger = getLogger(Listener.class);
    Collection<Task> list = Collections.synchronizedCollection(new ArrayList<>());

    public Listener() {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                list.forEach(task -> task.timerTask.run());
            }
        }, 60 * 1000, 60 * 1000);
    }

    @Override
    public void onReady(@NotNull ReadyEvent event) {
        logger.info("Ready!");
        if (Bot.DEV_MODE){
            var channel = event.getJDA().getTextChannelById(DEV_CHANNEL_ID);
            if (channel == null) return;
                    channel.sendMessage("Ready!").queue();
        } else {
            var channel = event.getJDA().getTextChannelById(CHANNEL_ID);
            if (channel == null) return;
            channel.sendMessage("<@&846784745073672252> Ready!").queue();
        }
    }

    @Override
    public void onGuildVoiceJoin(@NotNull GuildVoiceJoinEvent event) {
        new Thread(() -> {
            if (Bot.DEV_MODE && event.getChannelJoined().getIdLong() != Bot.DEV_VOICE_CHANNEL_ID) return;
            if (event.getMember().getUser().isBot()) return;

            try {
                DB.getInstance().setLastVoiceJoin(new Date().getTime(), event.getMember().getIdLong());
            } catch (SQLException | ClassNotFoundException e) {
                e.printStackTrace();
            }

            var id = event.getMember().getIdLong();

            list.add(new Task(new TimerTask() {

                @Override
                public void run() {

                    var voiceState = event.getMember().getVoiceState();
                    if (voiceState == null) return;
                    var voiceChannel = voiceState.getChannel();
                    if (voiceChannel == null) return;

                    var amountOfUsers = voiceChannel.getMembers().size();
                    var xp = ((3L * amountOfUsers) + 2) * Bot.happyHour;
                    try {
                        xp += xp * SkillsCommand.getSocialXPPMultiplier(id);
                    } catch (SQLException | ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                    try {
                        var db = DB.getInstance();

                        db.incrementXPVoice(xp, id);
                        db.incrementXP(xp, id);
                        logger.info(event.getMember().getEffectiveName());

                    } catch (ClassNotFoundException | SQLException e) {
                        e.printStackTrace();
                    }

                }
            }, id));
        }).start();
    }

    @Override
    public void onGuildVoiceLeave(@NotNull GuildVoiceLeaveEvent event) {
        new Thread(() -> list.removeIf(task -> task.id == event.getMember().getIdLong())).start();
    }

    @SuppressWarnings({"ConstantConditions", "DuplicatedCode"})
    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {
        new Thread(() -> {

            var channelID = event.getChannel().getIdLong();
            var channel = event.getChannel();
            var parent = channel.getParent();
            var categoryID = parent == null ? 0 : parent.getIdLong();
            var author = event.getAuthor();

            if (author.isBot()) return;
            if (event.isWebhookMessage()) return;
            if (Bot.DEV_MODE) {
                if (channelID != DEV_CHANNEL_ID) {
                    return;
                } else if (!Bot.DEV_MODE && (channelID == DEV_CHANNEL_ID || categoryID != 833734651070775338L)) {
                    return;
                }
            } else if (!Bot.DEV_MODE && channelID == DEV_CHANNEL_ID) {
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

        }).start();
    }

    @SuppressWarnings({"DuplicatedCode", "ConstantConditions"})
    @Override
    public void onGuildMessageReactionAdd(@NotNull GuildMessageReactionAddEvent event) {
        new Thread(() -> {

            var channelID = event.getChannel().getIdLong();
            var channel = event.getChannel();
            var parent = channel.getParent();
            var categoryID = parent == null ? 0 : parent.getIdLong();
            var reactingUser = event.getUser();

        /*
        Why is this so stupid!?
        Also there has to be a better way. At least regarding the AtomicReference...
         */
            AtomicReference<User> messageAuthor = new AtomicReference<>();
            event.retrieveMessage().queue(message -> messageAuthor.set(message.getAuthor()));
            while (messageAuthor.get() == null) Thread.onSpinWait();
            if (messageAuthor.get().isBot()) return;

            if (reactingUser.isBot()) return;
            if (Bot.DEV_MODE) {
                if (channelID != DEV_CHANNEL_ID) {
                    return;
                } else if (!Bot.DEV_MODE && (channelID == DEV_CHANNEL_ID || categoryID != 833734651070775338L)) {
                    return;
                }
            } else if (!Bot.DEV_MODE && channelID == DEV_CHANNEL_ID) {
                return;
            }

            try {

                var db = DB.getInstance();
                var id = reactingUser.getIdLong();

                var xp = 5 * Bot.happyHour;
                db.setLastReaction(new Date().getTime(), id);
                db.incrementXPReaction(xp, id);
                db.incrementXP(xp, id);

            } catch (ClassNotFoundException | SQLException e) {
                e.printStackTrace();
            }

        }).start();
    }
}

class Task {
    TimerTask timerTask;
    Long id;

    public Task(TimerTask timerTask, Long id) {
        this.timerTask = timerTask;
        this.id = id;
    }
}