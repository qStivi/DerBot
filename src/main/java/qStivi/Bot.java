package qStivi;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import org.slf4j.Logger;
import qStivi.commands.rpg.BlackjackCommand;
import qStivi.db.DB;
import qStivi.listeners.CommandManager;
import qStivi.listeners.ControlsManager;
import qStivi.listeners.Listener;

import javax.security.auth.login.LoginException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.*;

import static org.slf4j.LoggerFactory.getLogger;

public class Bot {
    public static boolean DEV_MODE = true;
    public static final long CHANNEL_ID = 742024523502846052L;
    public static final long DEV_CHANNEL_ID = 834012016481271908L;
    public static final long DEV_VOICE_CHANNEL_ID = 805955515241725983L;
    private static final Timer activityUpdate = new Timer();
    private static final String ACTIVITY = "Evolving...";
    private static final Logger logger = getLogger(Bot.class);

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
                .addEventListeners(new ControlsManager())
                .addEventListeners(new Listener())
                .addEventListeners(new BlackjackCommand())
                .enableCache(CacheFlag.VOICE_STATE)
                .setActivity(getActivity())
                .build();
        logger.info(String.valueOf(Bot.DEV_MODE));

        jda.addEventListener(new CommandManager());

        new DB();
        new Events(jda);
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