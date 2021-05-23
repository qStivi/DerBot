package qStivi;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import org.slf4j.Logger;
import qStivi.commands.rpg.BlackjackCommand;
import qStivi.db.DB;
import qStivi.listeners.CommandManager;
import qStivi.listeners.ControlsManager;
import qStivi.listeners.Listener;
import qStivi.listeners.ReactionRoles;

import javax.security.auth.login.LoginException;
import java.sql.SQLException;
import java.util.*;

import static org.slf4j.LoggerFactory.getLogger;

public class Bot {
    public static final long CHANNEL_ID = 742024523502846052L;
    public static final long DEV_CHANNEL_ID = 834012016481271908L;
    public static final long DEV_VOICE_CHANNEL_ID = 805955515241725983L;
    private static final Timer activityUpdate = new Timer();
    private static final String ACTIVITY = "Evolving...";
    private static final Logger logger = getLogger(Bot.class);
    public static boolean DEV_MODE = true;
    public static long happyHour = 1;

    public static void main(String[] args) throws LoginException, SQLException, ClassNotFoundException {
        logger.info(String.valueOf(Bot.DEV_MODE));
        var token = DEV_MODE ? Config.get("DEV_TOKEN") : Config.get("TOKEN");

        if (DEV_MODE) {
            logger.warn("Dev mode active!");
        }
        logger.info("Booting...");

        logger.info("Bot token: " + token);
        JDA jda = JDABuilder.createDefault(token)
                .addEventListeners(new ControlsManager(), new Listener(), new BlackjackCommand(), new ReactionRoles())
                .enableCache(CacheFlag.VOICE_STATE)
                .setActivity(getActivity())
                .build();
        logger.info(String.valueOf(Bot.DEV_MODE));

        jda.addEventListener(new CommandManager());

        new DB();
        new Events(jda);

        jda.getTextChannelById(843093823366365184L).editMessageById(845661569085079603L,
                "<@&843108657079779359> " + jda.getEmoteById(845646470686441493L).getAsMention() + "\n"
                        + "<@&755490059976966254> " + jda.getEmoteById(845646698767843329L).getAsMention() + "\n"
                        + "<@&846009044610580500> " + jda.getEmoteById(846008564673544267L).getAsMention() + "\n"
                        + "<@&846010998728425472> " + jda.getEmoteById(846010709380300821L).getAsMention() + "\n"
                        + "<@&750014132220330016> " + jda.getEmoteById(845646901512241153L).getAsMention() + "\n\n"
                        + "<@&843120253566320672> :brown_circle:\n"
                        + "<@&843120087245389824> :green_circle:\n"
                        + "<@&843120035853107210> :blue_circle:\n"
                        + "<@&843119963270545439> :purple_circle:\n"
                        + "<@&843120339508658186> :yellow_circle:\n"
                        + "<@&843120410190020628> :orange_circle:\n"
                        + "<@&843120454067945483> :red_circle:\n"
                        + "<@&843120566655647746> :black_circle:\n"
                        + "<@&843120664532877344> :white_circle:\n"
        ).queue(message -> {
            message.addReaction(Emotes.LOL).queue();
            message.addReaction(Emotes.MINECRAFT).queue();
            message.addReaction(Emotes.WARZONE).queue();
            message.addReaction(Emotes.APEX).queue();
            message.addReaction(Emotes.AMONGUS).queue();

            message.addReaction("\uD83D\uDFE4").queue();
            message.addReaction("\uD83D\uDFE2").queue();
            message.addReaction("\uD83D\uDD35").queue();
            message.addReaction("\uD83D\uDFE3").queue();
            message.addReaction("\uD83D\uDFE1").queue();
            message.addReaction("\uD83D\uDFE0").queue();
            message.addReaction("\uD83D\uDD34").queue();
            message.addReaction("⚫").queue();
            message.addReaction("⚪").queue();
        });

        logger.info(String.valueOf(Bot.DEV_MODE));
        if (DEV_MODE) return; // Don't continue if in development mode.
        logger.info(String.valueOf(Bot.DEV_MODE));

        activityUpdate.schedule(new TimerTask() {
            @Override
            public void run() {
                jda.getPresence().setActivity(getActivity());
            }
        }, 10 * 1000, 10 * 1000);
    }

    private static Activity getActivity() {
        List<Activity> activities = new ArrayList<>();
        activities.add(Activity.competing(ACTIVITY));
        activities.add(Activity.playing(ACTIVITY));
        activities.add(Activity.listening(ACTIVITY));
        activities.add(Activity.listening(ACTIVITY));
        activities.add(Activity.watching(ACTIVITY));
        Collections.shuffle(activities);

        return activities.get(0);
    }

}