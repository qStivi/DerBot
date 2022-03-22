package de.qStivi.commands.slash.gamble.blackjack;

import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;

public class BlackJackButtonHandler extends ListenerAdapter {

    @Override
    public void onButtonInteraction(@NotNull ButtonInteractionEvent event) {
        event.deferEdit().queue();

        var button = event.getButton();
        if (button.getId() == null) return;
        var args = button.getId().split("_");
        if (!args[0].equals("blackjack")) return;

        var member = event.getMember();
        if (member == null) throw new NullPointerException("Error while getting member!");
        var game = Games.getGameByPlayerId(member.getIdLong());

        var hook = event.getHook();

        try {

            if (args[1].equals("draw")) {
                game.draw(game.getPlayerHand());
                BlackjackCommand.displayGameState(game, event.getHook(), false);
            }

            if (args[1].equals("stand")) {
                while (game.getHandValue(game.getDealerHand()) < 17) {
                    game.draw(game.getDealerHand());
                }
                BlackjackCommand.displayGameState(game, event.getHook(), true);
            }

        } catch (SQLException e) {
            hook.editOriginal(e.getMessage()).queue();
        }
    }
}
