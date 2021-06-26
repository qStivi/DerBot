package de.qStivi;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicReference;

import static org.slf4j.LoggerFactory.getLogger;

public class Events {
    private static final Logger logger = getLogger(Events.class);
    JDA jda;
    Guild guild;
    TextChannel channel;
    boolean lottoAnnouncement = false;
    boolean resultSent = false;
    private boolean happyHourMessageSent = false;
    private boolean happyHourOverSent = false;
    private boolean sportsBets = false;


    private void getGuildChannelJDA(@NotNull JDA jda) {
        if (this.jda == null) {
            this.jda = jda;
        }
        if (guild == null) {
            while (guild == null) {
                guild = this.jda.getGuildById(Bot.GUILD_ID);
                logger.info("Guild is null");
            }
        }
        if (channel == null) {
            while (channel == null) {
                channel = guild.getTextChannelById(Bot.CHANNEL_ID);
                logger.info("Channel is null");
            }
        }
    }

    public Events(JDA jda) {
        getGuildChannelJDA(jda);
        run();
    }

    public void run() {
        new Thread(() -> new Timer().schedule(new TimerTask() {

            @Override
            public void run() {

                var now = LocalDateTime.now();
                var day = now.getDayOfWeek().name();
                var hour = now.getHour();
                var minute = now.getMinute();
//                var seconds = now.getSecond();

                // Lotto begin
                if (hour == 20 && !lottoAnnouncement) {

                    try {
                        announceLotto();
                    } catch (SQLException | ClassNotFoundException e) {
                        e.printStackTrace();
                        return;
                    }
                    lottoAnnouncement = true;
                }

                if (minute >= 5 && !resultSent && lottoAnnouncement) {
                    channel.sendMessage("https://media.giphy.com/media/Ps8XflhsT5EVa/giphy.gif").queue();

                    try {
                        sendResult();
                    } catch (SQLException | ClassNotFoundException e) {
                        e.printStackTrace();
                        return;
                    }

                    resultSent = true;
                }

                if (hour > 20) {
                    lottoAnnouncement = false;
                    resultSent = false;
                }
                // Lotto end

                // Happy half begin
                if (day.equalsIgnoreCase("friday") && hour == 19 && minute >= 30) {
                    Bot.happyHour = 2;
                    if (!happyHourMessageSent) {
                        channel.sendMessage("<@&846784745073672252> Happy half is starting now! :clock730:").queue();
                        happyHourMessageSent = true;
                        happyHourOverSent = false;
                    }
                } else {
                    Bot.happyHour = 1;
                    happyHourMessageSent = false;
                }
                if (day.equalsIgnoreCase("friday") && hour == 20) { // friday 20
                    if (!happyHourOverSent) {
                        channel.sendMessage("Happy half is over... :clock8:").queue();
                        happyHourOverSent = true;
                    }
                }
                // Happy half end

                // Sports bets
                if (hour == 23 && !sportsBets) {
                    ArrayList<Long> user = new ArrayList<>();
                    try {
                        user = DB.getInstance().getBetUserID();
                    } catch (SQLException | ClassNotFoundException e) {
                        e.printStackTrace();
                    }

                    for (Long aLong : user) {
                        try {
                            DB.getInstance().getProfit(aLong);
                        } catch (SQLException | ClassNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                    sportsBets = true;
                }

                if (hour == 0) {
                    sportsBets = false;
                }
                // Sports bets end

            }
        }, 0, 1000)).start();
    }

    private void announceLotto() throws SQLException, ClassNotFoundException {
        DB db;
        List<Long> players;
        db = DB.getInstance();
        players = db.getLottoParticipants();
        var sb = new StringBuilder();
        for (long id : players) {
            appendUsers(sb, id);
        }

        channel.sendMessage("<@&846784745073672252> " + sb + " \nThe raffle will be held in 5 minutes!").queue();
    }

    private void appendUsers(StringBuilder sb, long id) {
        AtomicReference<User> user = new AtomicReference<>(jda.getUserById(id));
        jda.retrieveUserById(id).queue(user::set);
        sb.append(user.get().getAsMention()).append(" ");
    }

    private void sendResult() throws SQLException, ClassNotFoundException {
        var number = ThreadLocalRandom.current().nextInt(1, 51);
        DB db;
        List<Long> winners;
        db = DB.getInstance();
        winners = db.getLottoParticipantsByVote(number);
        if (winners.isEmpty()) {
            channel.sendMessage("Better luck next time. No one won this raffle. The lucky number was " + number).queue();
        } else {

            long money = 0;

            var sb = new StringBuilder();
            for (long id : winners) {
                appendUsers(sb, id);
                money = Math.floorDiv(db.getLottoPool(), winners.size());
                db.incrementCommandMoney("lotto", money, id);
                db.incrementMoney(money, id);
                db.incrementGameWins("lotto", 1, id);
            }
            channel.sendMessage("Congratulations! " + sb + "has won the raffle. And will receive " + money + ":gem: each. The lucky number was " + number).queue();
            db.resetLottoPool();
        }
        db.resetLottoVotes();
    }
}
