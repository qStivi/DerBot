package qStivi.commands.rpg;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import qStivi.BlackJack;
import qStivi.Card;
import qStivi.ICommand;
import qStivi.db.DB;

import java.awt.*;
import java.sql.SQLException;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicReference;

@SuppressWarnings("ConstantConditions")
public class BlackjackCommand extends ListenerAdapter implements ICommand {

    Timer timer = new Timer();

    @Override
    public void handle(GuildMessageReceivedEvent event, String[] args) throws SQLException, ClassNotFoundException {
        var hook = event.getChannel();
        AtomicReference<String> messageId = new AtomicReference<>();
        hook.sendMessage("Loading...").queue(message -> messageId.set(message.getId()));
        // TODO put this in CommandManager
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                event.getMessage().delete().queue();
            }
        }, 3000);
        while (messageId.get() == null) Thread.onSpinWait();
        var db = new DB();
        long id = event.getAuthor().getIdLong();
        if (db.userDoesNotExists(id)) {
            db.insert("users", "id", id);
        }
        var money = db.selectLong("users", "money", "id", id);
        if (money < Long.parseLong(args[1])) {
            hook.editMessageById(String.valueOf(messageId), "You don't have enough money!").delay(DURATION).flatMap(Message::delete).queue();
            return;
        }
        if (Long.parseLong(args[1]) < 0) {
            hook.editMessageById(String.valueOf(messageId), "You can't do that, sorry.").delay(DURATION).flatMap(Message::delete).queue();
            return;
        }
//        db.increment("users", "command_times_blackjack", "id", id, 1);
        db.incrementCommandStatisticsOrGameStatisticsValue("CommandStatistics", "TimeRecognized", "CommandName", "UserID", 1, "BlackJack", id);


        var removed = BlackJack.games.removeIf(game -> game.user.getIdLong() == id);
//        if (removed) db.increment("users", "blackjack_loses", "id", id, 1);
        if (removed) db.incrementCommandStatisticsOrGameStatisticsValue("GameStatistics", "Loses", "GameName", "UserID", 1, "BlackJack", id);
        BlackJack.games.add(new BlackJack(1, messageId.get(), event.getAuthor(), hook, Long.parseLong(args[1])));
        BlackJack bj = null;
        for (BlackJack game : BlackJack.games) {
            if (game.user.getIdLong() == id) {
                bj = game;
            }
        }

        db.decrement("users", "money", "id", id, bj.bet);


        displayGameState(bj);

        if (bj.count(bj.player) == 21) {
            event.getChannel().clearReactionsById(bj.id).queue();
            BlackJack.games.remove(bj);
            bj.embed.setTitle("You won!");
//            db.increment("users", "money", "id", id, (long) Math.floor(bj.bet * 2.5));
            db.incrementUserDataValue("Money", "UserID", (long) Math.floor(bj.bet * 2.5), id);
            bj.embed.setColor(Color.green.brighter());
//            db.increment("users", "blackjack_wins", "id", id, 1);
            db.incrementCommandStatisticsOrGameStatisticsValue("GameStatistics", "Wins", "GameName", "UserID", 1, "BlackJack", id);
        } else {
            event.getChannel().addReactionById(bj.id, "\uD83E\uDD19\uD83C\uDFFD").queue();
            event.getChannel().addReactionById(bj.id, "✋\uD83C\uDFFD").queue();
        }

        hook.editMessageById(String.valueOf(messageId), args[1]).queue();
        hook.editMessageById(String.valueOf(messageId), bj.embed.build()).delay(DURATION).flatMap(Message::delete).queue();
    }

    @Override
    public void onGuildMessageReactionAdd(@NotNull GuildMessageReactionAddEvent event) {
        var user = event.getUser();
        DB db = null;
        try {
            db = new DB();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

        BlackJack bj = null;
        for (BlackJack game : BlackJack.games) {
            if (game.user.getId().equals(event.getUser().getId())) {
                bj = game;
            }
        }

        if (bj != null) {
            try {
                String messageId = event.getMessageId();
                if (messageId.equals(bj.id) && !Objects.requireNonNull(event.getUser()).isBot()) {
                    if (event.getReactionEmote().getEmoji().equals("\uD83E\uDD19\uD83C\uDFFD")) {
                        event.getReaction().removeReaction(user).queue();
                        if (bj.hit() > 21) {
                            endGame(event, db, bj, 0, "You Lost!");
                        }
                    }
                    if (event.getReactionEmote().getEmoji().equals("✋\uD83C\uDFFD")) {
                        event.getReaction().removeReaction(user).queue();
                        var dealerHandValue = bj.stand();
                        var playerHandValue = bj.count(bj.player);

                        if (dealerHandValue > 21 && playerHandValue <= 21)
                            endGame(event, db, bj, bj.bet * 2, "You won!");
                        else if (dealerHandValue < playerHandValue) endGame(event, db, bj, bj.bet * 2, "You won!");

                        else if (playerHandValue > 21 && dealerHandValue <= 21) endGame(event, db, bj, 0, "You Lost!");
                        else if (dealerHandValue > playerHandValue) endGame(event, db, bj, 0, "You Lost!");

                        else endGame(event, db, bj, bj.bet, "Draw.");
                    }
                    displayGameState(bj);
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }

    private void endGame(@NotNull GuildMessageReactionAddEvent event, DB db, BlackJack bj, long reward, String title) throws SQLException {
        var id = event.getUser().getIdLong();
        var messageId = event.getMessageId();
        bj.embed.setTitle(title);
//        db.increment("users", "money", "id", id, reward);
        db.incrementUserDataValue("Money", "UserID", reward, id);
        event.getChannel().clearReactionsById(messageId).queue();
        BlackJack.games.remove(bj);
        if (title.equalsIgnoreCase("you won!")) {
            bj.embed.setColor(Color.green.brighter());
//            db.increment("users", "blackjack_wins", "id", id, 1);
            db.incrementCommandStatisticsOrGameStatisticsValue("GameStatistics", "Wins", "GameName", "UserID", 1, "BlackJack", id);
        }
        if (title.equalsIgnoreCase("you lost!")) {
            bj.embed.setColor(Color.red.brighter());
//            db.increment("users", "blackjack_loses", "id", id, 1);
            db.incrementCommandStatisticsOrGameStatisticsValue("GameStatistics", "Loses", "GameName", "UserID", 1, "BlackJack", id);
        }
        if (title.equalsIgnoreCase("draw.")) {
            bj.embed.setColor(Color.magenta.darker());
//            db.increment("users", "blackjack_draws", "id", id, 1);
            db.incrementCommandStatisticsOrGameStatisticsValue("GameStatistics", "Draws", "GameName", "UserID", 1, "BlackJack", id);
        }
    }

    private void displayGameState(BlackJack bj) {
        bj.embed.setFooter(bj.user.getName());
        bj.embed.clearFields();
        bj.embed.addField("Dealer", String.valueOf(bj.count(bj.dealer)), true);

        StringBuilder dealerCards = new StringBuilder();
        var dealer = bj.dealer;
        for (Card card : dealer) {
            dealerCards.append("<:").append(card.emote).append("> ");
        }

        bj.embed.addField("", dealerCards.toString(), true);


        bj.embed.addBlankField(false);


        bj.embed.addField("Player", String.valueOf(bj.count(bj.player)), true);

        StringBuilder playerCards = new StringBuilder();
        var player = bj.player;
        for (Card card : player) {
            playerCards.append("<:").append(card.emote).append("> ");
        }

        bj.embed.addField("", playerCards.toString(), true);
        bj.hook.editMessageById(bj.id, bj.embed.build()).queue();
    }

    @NotNull
    @Override
    public String getName() {
        return "bj";
    }

    @NotNull
    @Override
    public String getDescription() {
        return "Don't count the cards!";
    }

    @Override
    public long getXp() {
        return 3;
    }
}
