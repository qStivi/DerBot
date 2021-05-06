package qStivi;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.User;
import org.slf4j.Logger;
import qStivi.db.DB;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicReference;

import static org.slf4j.LoggerFactory.getLogger;

public class Lotto {
    Timer lotto = new Timer();
    private static final Logger logger = getLogger(DB.class);
    boolean announcement = false;
    boolean gif = false;
    boolean win = false;

    public Lotto(JDA jda) throws InterruptedException {

        Thread.sleep(3000);
        var guild = jda.getGuildById(703363806356701295L);
        if (guild == null) {
            logger.error("Guild is null!");
            return;
        }
        var channel = guild.getTextChannelById(742024523502846052L);
        if (channel == null) {
            logger.error("Channel is null!");
            return;
        }

        lotto.schedule(new TimerTask() {
            @Override
            public void run() {
                var now = LocalDateTime.now();
                var day = now.getDayOfWeek().name();
                var hour = now.getHour();
                var minute = now.getMinute();
                var seconds = now.getSecond();

                if (seconds <= 50) gif = false;
                if (/*(day.equals("WEDNESDAY") || day.equals("FRIDAY")) && */(hour == 19 && minute == 59 && seconds > 50) && !gif) {
                    gif = true;
                    channel.sendMessage("https://media.giphy.com/media/Ps8XflhsT5EVa/giphy.gif").queue();
                }

                if (seconds >= 10) win = false;
                if (/*(day.equals("WEDNESDAY") || day.equals("FRIDAY")) && */(hour == 20 && minute == 0 && seconds < 10) && !win) {
                    win = true;
                    var number = ThreadLocalRandom.current().nextInt(1, 51);
                    DB db;
                    List<Long> winners;
                    try {
                        db = new DB();
                        winners = db.getLottoParticipantsByVote(number);
                    } catch (ClassNotFoundException | SQLException e) {
                        e.printStackTrace();
                        return;
                    }
                    if (winners.isEmpty()) {
                        channel.sendMessage("Better luck next time. No one won this raffle. The lucky number was " + number).queue();
                        try {
                            db.resetLottoVotes();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        return;
                    } else {

                        int money = 0;

                        var sb = new StringBuilder();
                        for (long id : winners) {
                            AtomicReference<User> user = new AtomicReference<>(jda.getUserById(id));
                            jda.retrieveUserById(id).queue(user::set);
                            while(user.get() == null) Thread.onSpinWait();
                            sb.append(user.get().getAsMention()).append(" ");
                            try {
                                money = Math.floorDiv(db.getLottoPool(), winners.size());
                                db.incrementCommandMoney("lotto", money, id);
                                db.incrementMoney(money, id);
                                db.incrementGameWins("lotto", 1, id);
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        }
                        channel.sendMessage("Congratulations! " + sb + "has won the raffle. And will receive " + money + ":gem: each. The lucky number was " + number).queue();
                    }
                    try {
                        db.resetLottoPool();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }

                if (seconds >= 10) announcement = false;
                if (/*(day.equals("WEDNESDAY") || day.equals("FRIDAY")) && */(hour == 19 && minute == 55 && seconds < 10) && !announcement) {
                    announcement = true;
                    DB db;
                    List<Long> players;
                    try {
                        db = new DB();
                        players = db.getLottoParticipants();
                    } catch (ClassNotFoundException | SQLException e) {
                        e.printStackTrace();
                        return;
                    }
                    var sb = new StringBuilder();
                    for (long id : players){
                        AtomicReference<User> user = new AtomicReference<>(jda.getUserById(id));
                        jda.retrieveUserById(id).queue(user::set);
                        while(user.get() == null) Thread.onSpinWait();
                        sb.append(user.get().getAsMention()).append(" ");
                    }

                    channel.sendMessage(sb + " \nThe raffle will be held in 5 minutes!").queue();
                }
            }
        }, 10 * 1000, 1000);
    }
}
