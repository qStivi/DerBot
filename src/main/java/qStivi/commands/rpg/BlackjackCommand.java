package qStivi.commands.rpg;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import qStivi.*;

import java.awt.*;
import java.sql.SQLException;
import java.util.Date;
import java.util.Objects;

@SuppressWarnings({"ConstantConditions", "DuplicatedCode"})
public class BlackjackCommand extends ListenerAdapter implements ICommand {

    private long xp;

    @Override
    public void handle(GuildMessageReceivedEvent event, String[] args, DB db, Message reply) throws SQLException, ClassNotFoundException {
        xp = 0;
        long bet;

        try {
            bet = Long.parseLong(args[1]);
        } catch (NumberFormatException | ArrayIndexOutOfBoundsException ignored) {
            reply.editMessage("Please enter a valid number.").queue();
            return;
        }

        if (args.length < 2 || bet > 1000000) return;
        var hook = event.getChannel();
        long id = event.getAuthor().getIdLong();
        var money = db.getMoney(id);
        if (money < bet) {
            reply.editMessage("You don't have enough money!").queue();
            return;
        }
        if (Long.parseLong(args[1]) < 0) {
            reply.editMessage("You can't do that, sorry.").queue();
            return;
        }
        db.incrementGamePlays(getName(), 1, id);

        var removed = BlackJack.games.removeIf(game -> game.user.getIdLong() == id);
        if (removed) db.incrementGameLoses(getName(), 1, id);
        BlackJack.games.add(new BlackJack(1, reply, event.getAuthor(), hook, Long.parseLong(args[1])));
        BlackJack bj = null;
        for (BlackJack game : BlackJack.games) {
            if (game.user.getIdLong() == id) {
                bj = game;
            }
        }

        db.decrementMoney(bj.bet, id);

        displayGameState(bj);

        if (bj.count(bj.player) == 21) {
            endGame(event, db, bj, (long) (bj.bet * 2.5), "You won!");
        } else {
            reply.addReaction("\uD83E\uDD19\uD83C\uDFFD").queue();
            reply.addReaction("✋\uD83C\uDFFD").queue();
        }

        reply.editMessage(args[1]).queue();
        reply.editMessage(bj.embed.build()).queue();

        xp = 3 + (long) (3 * SkillsCommand.getGambleXPMultiplier(event.getAuthor().getIdLong()));
    }

    @Override
    public void onGuildMessageReactionAdd(@NotNull GuildMessageReactionAddEvent event) {
        var user = event.getUser();
        DB db = null;
        try {
            db = DB.getInstance();
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
                if (messageId.equals(bj.reply.getId()) && !Objects.requireNonNull(event.getUser()).isBot()) {
                    if (event.getReactionEmote().getEmoji().equals("\uD83E\uDD19\uD83C\uDFFD")) {
                        event.getReaction().removeReaction(user).queue();
                        if (bj.hit() > 21) {
                            endGame(event, db, bj, 0, "You Lost!");
                            db.incrementLottoPool(bj.bet);
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
                            db.incrementLottoPool(bj.bet);
                        } else if (dealerHandValue > playerHandValue) {
                            endGame(event, db, bj, 0, "You Lost!");
                            db.incrementLottoPool(bj.bet);
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

    @SuppressWarnings("SameParameterValue")
    private void endGame(@NotNull GuildMessageReceivedEvent event, DB db, BlackJack bj, long reward, String title) throws SQLException {
        reward = reward * Bot.happyHour;
        var id = event.getMessage().getAuthor().getIdLong();
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
        bj.reply.editMessage(bj.embed.build()).queue();
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
