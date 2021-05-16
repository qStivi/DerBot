package qStivi.commands.rpg;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import qStivi.BlackJack;
import qStivi.Bot;
import qStivi.Card;
import qStivi.ICommand;
import qStivi.db.DB;

import java.awt.*;
import java.sql.SQLException;
import java.util.Date;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

@SuppressWarnings("ConstantConditions")
public class BlackjackCommand extends ListenerAdapter implements ICommand {

    private long xp;

    @Override
    public void handle(GuildMessageReceivedEvent event, String[] args) throws SQLException, ClassNotFoundException {
        var hook = event.getChannel();
        AtomicReference<String> messageId = new AtomicReference<>();
        hook.sendMessage("Loading...").queue(message -> messageId.set(message.getId()));
        while (messageId.get() == null) Thread.onSpinWait();
        var db = new DB();
        long id = event.getAuthor().getIdLong();
        var money = db.getMoney(id);
        if (money < Long.parseLong(args[1])) {
            hook.editMessageById(String.valueOf(messageId), "You don't have enough money!").queue();
            return;
        }
        if (Long.parseLong(args[1]) < 0) {
            hook.editMessageById(String.valueOf(messageId), "You can't do that, sorry.").queue();
            return;
        }
        db.incrementGamePlays(getName(), 1, id);

        var removed = BlackJack.games.removeIf(game -> game.user.getIdLong() == id);
        if (removed) db.incrementGameLoses(getName(), 1, id);
        BlackJack.games.add(new BlackJack(1, messageId.get(), event.getAuthor(), hook, Long.parseLong(args[1])));
        BlackJack bj = null;
        for (BlackJack game : BlackJack.games) {
            if (game.user.getIdLong() == id) {
                bj = game;
            }
        }

        db.decrementMoney(bj.bet, id);

        displayGameState(bj);

        if (bj.count(bj.player) == 21) {
            event.getChannel().clearReactionsById(bj.id).queue();
            BlackJack.games.remove(bj);
            bj.embed.setTitle("You won!");
            db.incrementMoney((long) Math.floor(bj.bet * 2.5), id);
            bj.embed.setColor(Color.green.brighter());
            db.incrementGameWins("BlackJack", 1, id);
        } else {
            event.getChannel().addReactionById(bj.id, "\uD83E\uDD19\uD83C\uDFFD").queue();
            event.getChannel().addReactionById(bj.id, "✋\uD83C\uDFFD").queue();
        }

        hook.editMessageById(String.valueOf(messageId), args[1]).queue();
        hook.editMessageById(String.valueOf(messageId), bj.embed.build()).queue();

        xp = 3 + (long) (3 * SkillsCommand.getGambleXPMultiplier(event.getAuthor().getIdLong()));
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
                            db.incrementLottoPool(bj.bet/2);
                        }
                    }
                    if (event.getReactionEmote().getEmoji().equals("✋\uD83C\uDFFD")) {
                        event.getReaction().removeReaction(user).queue();
                        var dealerHandValue = bj.stand();
                        var playerHandValue = bj.count(bj.player);

                        if (dealerHandValue > 21 && playerHandValue <= 21)
                            endGame(event, db, bj, bj.bet * 2, "You won!");
                        else if (dealerHandValue < playerHandValue) endGame(event, db, bj, bj.bet * 2, "You won!");

                        else if (playerHandValue > 21 && dealerHandValue <= 21) {
                            endGame(event, db, bj, 0, "You Lost!");
                            db.incrementLottoPool(bj.bet/2);
                        } else if (dealerHandValue > playerHandValue) {
                            endGame(event, db, bj, 0, "You Lost!");
                            db.incrementLottoPool(bj.bet/2);
                        } else endGame(event, db, bj, bj.bet, "Draw.");
                    }
                    displayGameState(bj);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void endGame(@NotNull GuildMessageReactionAddEvent event, DB db, BlackJack bj, long reward, String title) throws SQLException {
        reward = reward * Bot.happyHour;
        var id = event.getUser().getIdLong();
        var messageId = event.getMessageId();
        bj.embed.setTitle(title);
        db.incrementMoney(reward, id);
        db.incrementCommandMoney(getName(), reward, id);
        db.setGameLastPlayed(getName(), new Date().getTime(), id);
        event.getChannel().clearReactionsById(messageId).queue();
        BlackJack.games.remove(bj);
        if (title.equalsIgnoreCase("you won!")) {
            bj.embed.setColor(Color.green.brighter());
            db.incrementGameWins(getName(), 1, id);
        }
        if (title.equalsIgnoreCase("you lost!")) {
            bj.embed.setColor(Color.red.brighter());
            db.incrementGameLoses(getName(), 1, id);
        }
        if (title.equalsIgnoreCase("draw.")) {
            bj.embed.setColor(Color.magenta.darker());
            db.incrementGameDraws(getName(), 1, id);
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
        return xp;
    }
}
