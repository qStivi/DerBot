package qStivi.listeners;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import qStivi.Bot;
import qStivi.ICommand;
import qStivi.commands.*;
import qStivi.commands.music.*;
import qStivi.commands.rpg.*;
import qStivi.DB;
import qStivi.commands.rpg.slots.SlotsCommand;

import java.sql.SQLException;
import java.text.Normalizer;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import static org.slf4j.LoggerFactory.getLogger;
import static qStivi.Bot.DEV_CHANNEL_ID;

public class CommandManager extends ListenerAdapter {
    private static final Logger logger = getLogger(CommandManager.class);

    public final List<ICommand> commandList = new ArrayList<>();
    public final BlockingQueue<Command> queue = new LinkedBlockingQueue<>();

    public CommandManager() {
        logger.debug("Registering commands.");
        commandList.add(new TestCommand());
        commandList.add(new RollCommand());
        commandList.add(new StopCommand());
        commandList.add(new ContinueCommand());
        commandList.add(new DndCommand());
        commandList.add(new PauseCommand());
        commandList.add(new RepeatCommand());
        commandList.add(new SkipCommand());
        commandList.add(new CleanCommand());
        commandList.add(new JoinCommand());
        commandList.add(new LeaveCommand());
//        commandList.add(new PingCommand());
        commandList.add(new RedditCommand());
        commandList.add(new PlayCommand());
        commandList.add(new ShutdownCommand());
        commandList.add(new StatsCommand());
        commandList.add(new Top10Command());
        commandList.add(new WorkCommand());
        commandList.add(new BlackjackCommand());
        commandList.add(new moneyCommand());
        commandList.add(new DonateCommand());
        commandList.add(new BegCommand());
        commandList.add(new SlotsCommand());
        commandList.add(new LottoCommand());
        commandList.add(new SkillsCommand());
        commandList.add(new BetCommand());
        commandList.add(new SalesCommand());
        commandList.add(new InfoCommand());
        commandList.add(new ResultCommand());

        var timer = new Timer();

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                var thread = new Thread(() -> {
                    try {
                        var command = queue.poll(1, TimeUnit.SECONDS);
                        if (command != null) command.handle();
                    } catch (SQLException | ClassNotFoundException | InterruptedException e) {
                        e.printStackTrace();
                    }
                });
                thread.start();
            }
        }, 0, 500);
    }

    public static String cleanForCommand(String str) {
        str = str.toLowerCase().strip();
        str = Normalizer.normalize(str, Normalizer.Form.NFKD);
        str = str.replaceAll("[^a-z0-9A-Z -]", ""); // Remove all non valid chars
        str = str.replaceAll("[ \\t]+", " ").trim(); // convert multiple spaces into one space
        return str;
    }

    @SuppressWarnings({"ConstantConditions", "DuplicatedCode"})
    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {

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
            } else if (!Bot.DEV_MODE)
                if (channelID == DEV_CHANNEL_ID || categoryID != 833734651070775338L) {
                    return;
                }
        } else if (!Bot.DEV_MODE) {
            if (channelID == DEV_CHANNEL_ID) {
                return;
            } else if (categoryID != 833734651070775338L) {
                return;
            }
        }


        try {
            if (isCommand(event)) {

                var message = cleanForCommand(event.getMessage().getContentRaw());
                var args = message.split(" ");

                for (var command : commandList) {
                    if (command.getName().equals(args[0])) {

                        queue.put(new Command(command, event, args));
                        logger.debug("Command queued.");

                    }
                }
            } else {
                var id = author.getIdLong();
                var now = new Date().getTime();
                var db = DB.getInstance();

                @SuppressWarnings("PointlessArithmeticExpression") var xp = 1 * Bot.happyHour;
                db.setLastChat(now, id);
                db.incrementXPChat(xp, id);
                db.incrementXP(xp, id);
            }
        } catch (ClassNotFoundException | SQLException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private boolean isCommand(GuildMessageReceivedEvent event) {
        return event.getMessage().getContentRaw().toLowerCase().strip().startsWith("/");
    }
}

class Command {
    ICommand command;
    GuildMessageReceivedEvent event;
    String[] args;
    DB db = DB.getInstance();

    public Command(ICommand command, GuildMessageReceivedEvent event, String[] args) throws SQLException, ClassNotFoundException {
        this.command = command;
        this.event = event;
        this.args = args;
    }

    void handle() throws SQLException, ClassNotFoundException, InterruptedException {
        var reply = event.getMessage().reply("Loading...").complete();

        this.command.handle(this.event, this.args, this.db, reply);

        var name = command.getName();
        var id = event.getAuthor().getIdLong();
        var db = DB.getInstance();

        var xp = command.getXp() * Bot.happyHour;
        db.incrementCommandXP(name, xp, id);
        db.incrementXP(xp, id);
        if (!name.equalsIgnoreCase("slots")) db.incrementCommandTimesHandled(name, 1, id);
        if (!name.equalsIgnoreCase("work")) db.setCommandLastHandled(name, new Date().getTime(), id);

        System.gc(); // Because Memory usage gets crazy after a while
    }
}