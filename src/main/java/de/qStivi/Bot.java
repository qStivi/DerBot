package de.qStivi;

import de.qStivi.commands.slash.ISlashCommand;
import de.qStivi.commands.slash.SlashCommandHandler;
import de.qStivi.commands.slash.gamble.blackjack.BlackJackButtonHandler;
import de.qStivi.commands.slash.skills.LevelUpEvent;
import de.qStivi.events.ButtonTestEvent;
import de.qStivi.events.SelectMenuTestEvent;
import de.qStivi.listeners.Hmmm;
import de.qStivi.listeners.ReactionRoles;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import org.slf4j.Logger;

import javax.security.auth.login.LoginException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Timer;

import static org.slf4j.LoggerFactory.getLogger;

public class Bot {

    public static final long GUILD_ID = Long.parseLong(Config.get("GUILD_ID"));
    public static final long CATEGORY_ID = Long.parseLong(Config.get("CATEGORY_ID"));
    public static final long CHANNEL_ID = Long.parseLong(Config.get("CHANNEL_ID"));
    public static final long DEV_CHANNEL_ID = Long.parseLong(Config.get("DEV_CHANNEL_ID"));
    public static final long DEV_VOICE_CHANNEL_ID = Long.parseLong(Config.get("DEV_VOICE_CHANNEL_ID"));
    private static final Timer activityUpdate = new Timer();
    private static final String ACTIVITY = "Evolving...";
    private static final Logger logger = getLogger(Bot.class);
    public static boolean DEV_MODE;
    public static long happyHour = 1;
    public static JDA JDA;

    public static void main(String[] args) throws LoginException, SQLException, ClassNotFoundException {

        if (args.length > 0) {
            for (String arg : args) {
                DEV_MODE = arg.equalsIgnoreCase("dev");
            }
        }

//        for (int i = 0; i < 50; i++) {
//            System.out.println(Nomen.randomName());
//        }

        logger.info(String.valueOf(Bot.DEV_MODE));
        var token = DEV_MODE ? Config.get("DEV_TOKEN") : Config.get("TOKEN");

        if (DEV_MODE) {
            logger.warn("Dev mode active!");
        }
        logger.info("Booting...");

        logger.info("Bot token: " + token);
        JDA = JDABuilder.createLight(token).addEventListeners(
//                        new Listener(),
                new ReactionRoles(),
//                        new EventsPreprocessor(),
                SlashCommandHandler.getInstance(), new LevelUpEvent(), new SelectMenuTestEvent(), new ButtonTestEvent(), new Hmmm(), new BlackJackButtonHandler(), new MemberCountUpdater()).enableCache(CacheFlag.VOICE_STATE, CacheFlag.EMOTE).enableIntents(GatewayIntent.GUILD_MESSAGE_REACTIONS, GatewayIntent.GUILD_MESSAGES, GatewayIntent.GUILD_VOICE_STATES, GatewayIntent.GUILD_MEMBERS, GatewayIntent.GUILD_PRESENCES).setChunkingFilter(ChunkingFilter.NONE).setMemberCachePolicy(MemberCachePolicy.ALL).setLargeThreshold(250).setActivity(getActivity()).build();

        DB.getInstance();

//        new Items();

        var uc = JDA.updateCommands();
        var commands = SlashCommandHandler.getInstance().getCommands();
        //noinspection ResultOfMethodCallIgnored
        uc.addCommands(commands.stream().map(ISlashCommand::getCommand).toList());
        uc.complete();

//        db.insertItem(219108246143631364L, new DevItem());
//        db.insertItem(219108246143631364L, new DevItem());
//        db.insertItem(219108246143631364L, new DevItem());

//        if (DEV_MODE) return; // Don't continue if in development mode.

//        new Events(jda);

//        activityUpdate.schedule(new TimerTask() {
//            @Override
//            public void run() {
//                jda.getPresence().setActivity(getActivity());
//            }
//        }, 10 * 1000, 10 * 1000);
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
