package qStivi;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import qStivi.db.DB;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicReference;

public class Events {
    JDA jda;
    AtomicReference<TextChannel> channel = new AtomicReference<>();
    boolean lottoAnnouncement = false;
    boolean resultSent = false;
    private boolean happyHourMessageSent = false;
    private boolean happyHourOverSent = false;

    public Events(JDA jda) {
        this.jda = jda;

        AtomicReference<Guild> guild = new AtomicReference<>();
        guild.set(jda.getGuildById(703363806356701295L));
        while (guild.get() == null) {
            Thread.onSpinWait();
        }
        channel.set(guild.get().getTextChannelById(742024523502846052L));
        while (channel.get() == null) {
            Thread.onSpinWait();
        }

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
                    channel.get().sendMessage("https://media.giphy.com/media/Ps8XflhsT5EVa/giphy.gif").queue();

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
                        channel.get().sendMessage("Happy half is starting now! :clock730:").queue();
                        happyHourMessageSent = true;
                        happyHourOverSent = false;
                    }
                } else {
                    Bot.happyHour = 1;
                    happyHourMessageSent = false;
                }
                if (day.equalsIgnoreCase("friday") && hour == 20) {
                    if (!happyHourOverSent) {
                        channel.get().sendMessage("Happy half is over... :clock8:").queue();
                        happyHourOverSent = true;
                    }
                }
                // Happy half end


            }
        }, 0, 1000)).start();
    }

    private void announceLotto() throws SQLException, ClassNotFoundException {
        DB db;
        List<Long> players;
        db = new DB();
        players = db.getLottoParticipants();
        var sb = new StringBuilder();
        for (long id : players) {
            appendUsers(sb, id);
        }

        channel.get().sendMessage(sb + " \nThe raffle will be held in 5 minutes!").queue();
    }

    private void appendUsers(StringBuilder sb, long id) {
        AtomicReference<User> user = new AtomicReference<>(jda.getUserById(id));
        jda.retrieveUserById(id).queue(user::set);
        while (user.get() == null) Thread.onSpinWait();
        sb.append(user.get().getAsMention()).append(" ");
    }

    private void sendResult() throws SQLException, ClassNotFoundException {
        var number = ThreadLocalRandom.current().nextInt(1, 51);
        DB db;
        List<Long> winners;
        db = new DB();
        winners = db.getLottoParticipantsByVote(number);
        if (winners.isEmpty()) {
            channel.get().sendMessage("Better luck next time. No one won this raffle. The lucky number was " + number).queue();
        } else {

            long money = 0;

            var sb = new StringBuilder();
            for (long id : winners) {
                appendUsers(sb, id);
                money = Math.floorDiv(db.getLottoPool(), (long) winners.size());
                db.incrementCommandMoney("lotto", money, id);
                db.incrementMoney(money, id);
                db.incrementGameWins("lotto", 1, id);
            }
            channel.get().sendMessage("Congratulations! " + sb + "has won the raffle. And will receive " + money + ":gem: each. The lucky number was " + number).queue();
            db.resetLottoPool();
        }
        db.resetLottoVotes();
    }
}
