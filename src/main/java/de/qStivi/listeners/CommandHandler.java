//package de.qStivi.listeners;
//
//import de.qStivi.Bot;
//import de.qStivi.DB;
//import de.qStivi.ICommand;
//import de.qStivi.Util;
//import de.qStivi.commands.*;
//import de.qStivi.commands.rpg.*;
//import de.qStivi.commands.rpg.slots.SlotsCommand;
//import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
//import org.jetbrains.annotations.NotNull;
//import org.slf4j.Logger;
//
//import java.sql.SQLException;
//import java.text.Normalizer;
//import java.util.*;
//import java.util.concurrent.BlockingQueue;
//import java.util.concurrent.LinkedBlockingQueue;
//import java.util.concurrent.TimeUnit;
//
//import static de.qStivi.Bot.DEV_CHANNEL_ID;
//import static org.slf4j.LoggerFactory.getLogger;
//
//public class CommandHandler implements IGuildMessageReceivedEvent {
//    private static final Logger logger = getLogger(CommandHandler.class);
//
//    public final List<ICommand> commandList = new ArrayList<>();
//    public final BlockingQueue<Command> queue = new LinkedBlockingQueue<>();
//
//    public CommandHandler() {
//        logger.debug("Registering commands.");
//        commandList.add(new RollCommand());
////        commandList.add(new StopCommand());
////        commandList.add(new ContinueCommand());
////        commandList.add(new DndCommand());
////        commandList.add(new PauseCommand());
////        commandList.add(new RepeatCommand());
////        commandList.add(new SkipCommand());
//        commandList.add(new CleanCommand());
////        commandList.add(new JoinCommand());
////        commandList.add(new LeaveCommand());
////        commandList.add(new PingCommand());
//        commandList.add(new RedditCommand());
////        commandList.add(new PlayCommand());
//        commandList.add(new ShutdownCommand());
//        commandList.add(new StatsCommand());
//        commandList.add(new Top10Command());
//        commandList.add(new WorkCommand());
//        commandList.add(new BlackjackCommand());
//        commandList.add(new moneyCommand());
//        commandList.add(new DonateCommand());
//        commandList.add(new BegCommand());
//        commandList.add(new SlotsCommand());
//        commandList.add(new LottoCommand());
//        commandList.add(new SkillsCommand());
//        commandList.add(new BetCommand());
//        commandList.add(new SalesCommand());
//        commandList.add(new InfoCommand());
//        commandList.add(new ScoreCommand());
//        commandList.add(new InventoryCommand());
//        commandList.add(new UseCommand());
//        commandList.add(new TradeCommand());
//        commandList.add(new ShopCommand());
//        commandList.add(new BuyCommand());
//
//        var timer = new Timer();
//
//        timer.schedule(new TimerTask() {
//            @Override
//            public void run() {
//                var thread = new Thread(() -> {
//                    try {
//                        var command = queue.poll(1, TimeUnit.SECONDS);
//                        if (command != null) command.handle();
//                    } catch (SQLException | ClassNotFoundException | InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                });
//                thread.start();
//            }
//        }, 0, 500);
//    }
//
//    public static String cleanForCommand(String str) {
//        str = str.toLowerCase().strip();
//        str = Normalizer.normalize(str, Normalizer.Form.NFKD);
//        // TODO Make sure to not break anything before doing this
////        str = str.replaceAll("??", "ae").replaceAll("??", "oe").replaceAll("??", "ue");
//        str = str.replaceAll("[^a-z0-9A-Z -]", ""); // Remove all non valid chars
//        str = str.replaceAll("[ \\t]+", " ").trim(); // convert multiple spaces into one space
//        return str;
//    }
//
//    @SuppressWarnings({"ConstantConditions", "DuplicatedCode"})
//    @Override
//    public void handle(@NotNull GuildMessageReceivedEvent event) {
//
//
//
//        var channelID = event.getChannel().getIdLong();
//        var channel = event.getChannel();
//        var parent = channel.getParent();
//        var categoryID = parent == null ? 0 : parent.getIdLong();
//        var author = event.getAuthor();
//
//        if (Bot.DEV_MODE) {
//            if (channelID != DEV_CHANNEL_ID) {
//                return;
//            } else if (!Bot.DEV_MODE)
//                if (channelID == DEV_CHANNEL_ID || categoryID != 833734651070775338L) {
//                    return;
//                }
//        } else if (!Bot.DEV_MODE) {
//            if (channelID == DEV_CHANNEL_ID) {
//                return;
//            } else if (categoryID != 833734651070775338L) {
//                return;
//            }
//        }
//
//
//        try {
//            if (isCommand(event)) {
//
//                String message;
//                String potentialLink = "";
//
//                // TODO there has to be a better way to do this
//                // Play command exception
//                if (event.getMessage().getContentRaw().toLowerCase().split(" ").length >= 2) {
//                    potentialLink = event.getMessage().getContentRaw().toLowerCase().split(" ")[1];
//                }
//                if (!Util.isValidLink(potentialLink)) {
//                    message = cleanForCommand(event.getMessage().getContentRaw());
//                } else {
//                    message = event.getMessage().getContentRaw().replaceFirst("/", "");
//                }
//                if (message == null) {
//                    logger.error("Message is null!");
//                    return;
//                }
//                // Play command exception end
//
//                var args = message.split(" ");
//
//                for (var command : commandList) {
//                    if (command.getName().equals(args[0])) {
//
//                        queue.put(new Command(command, event, args));
//                        logger.debug("Command queued.");
//
//                    }
//                }
//            } else {
//                var id = author.getIdLong();
//                var now = new Date().getTime();
//                var db = DB.getInstance();
//
//                @SuppressWarnings("PointlessArithmeticExpression") var xp = 1 * Bot.happyHour;
//                db.setLastChat(now, id);
//                db.incrementXPChat(xp, id);
//                db.incrementXP(xp, id);
//            }
//        } catch (ClassNotFoundException | SQLException | InterruptedException e) {
//            e.printStackTrace();
//        }
//    }
//
//    private boolean isCommand(GuildMessageReceivedEvent event) {
//        return event.getMessage().getContentRaw().toLowerCase().strip().startsWith("/");
//    }
//}
