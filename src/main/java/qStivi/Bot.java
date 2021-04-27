package qStivi;

import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import org.slf4j.Logger;
import qStivi.commands.rpg.BlackjackCommand;
import qStivi.db.DB;
import qStivi.listeners.CommandManager;
import qStivi.listeners.ControlsManager;
import qStivi.listeners.Listener;
import qStivi.listeners.UserManager;

import javax.security.auth.login.LoginException;
import java.time.LocalDateTime;
import java.util.*;

import static org.slf4j.LoggerFactory.getLogger;

public class Bot {
    public static final boolean DEV_MODE = true;
    public static final String CHANNEL_ID = Config.get("CHANNEL_ID");
    public static final String DEV_CHANNEL_ID = Config.get("DEV_CHANNEL_ID");
    public static final String DEV_VOICE_CHANNEL_ID = Config.get("DEV_VOICE_CHANNEL_ID");
    private static final Timer reminder = new Timer();
    private static final Timer activityUpdate = new Timer();
    private static final String ACTIVITY = "Evolving...";
    private static final Logger logger = getLogger(Bot.class);

    public static void main(String[] args) throws LoginException {
        var token = DEV_MODE ? Config.get("DEV_TOKEN") : Config.get("TOKEN");

        if (DEV_MODE) {
            logger.warn("Dev mode active!");
        }
        logger.info("Booting...");
        var db = new DB();
        db.createNewDatabase("bot");
        db.createNewTable("users",
                "money integer default 1000," +
                        "xp integer default 0," +
                        "last_worked integer default 0," +
                        "last_chat_message integer default 0," +
                        "last_command integer default 0," +
                        "last_reaction integer default 0," +
                        "last_command_xp integer default 0," +
                        "last_donated integer default 0," +
                        "command_times_blackjack integer default 0," +
                        "xp_reaction integer default 0," +
                        "xp_voice integer default 0," +
                        "xp_chat integer default 0," +
                        "xp_command integer default 0," +
                        "blackjack_wins integer default 0," +
                        "blackjack_loses integer default 0," +
                        "blackjack_draws integer default 0"
        );

        logger.info("Bot token: " + token);
        var jda = JDABuilder.createDefault(token)
                .addEventListeners(new ControlsManager())
                .addEventListeners(new Listener())
                .addEventListeners(new UserManager())
                .addEventListeners(new BlackjackCommand())
                .setActivity(getActivity())
                .build();

        jda.addEventListener(new CommandManager());
        jda.updateCommands().addCommands().queue();

        if (DEV_MODE) return; // Don't continue if in development mode.

        activityUpdate.schedule(new TimerTask() {
            @Override
            public void run() {
                jda.getPresence().setActivity(getActivity());
            }
        }, 10 * 1000, 10 * 1000);


        reminder.schedule(new TimerTask() {
            @Override
            public void run() {
                var now = LocalDateTime.now();
                var tag = now.getDayOfWeek().name();
                var stunde = now.getHour();
                var minute = now.getMinute();
                var seconds = now.getSecond();
                if (tag.equals("WEDNESDAY") && stunde == 18 && minute == 18 && seconds == 0) {
                    var channel = jda.getTextChannelById("755490778922352801");
                    if (channel != null) channel.sendMessage("D&D Today!").mentionRoles("755490137118474270").queue();
                }
            }
        }, 5 * 1000, 1000);
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