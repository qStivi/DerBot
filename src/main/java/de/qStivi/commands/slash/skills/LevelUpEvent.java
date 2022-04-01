package de.qStivi.commands.slash.skills;

import de.qStivi.enitities.player.Players;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;

public class LevelUpEvent extends ListenerAdapter {

    @Override
    public void onButtonInteraction(@NotNull ButtonInteractionEvent event) {
        var button = event.getButton();
        var id = button.getId();
        if (id == null) return;
        if (!id.equals("levelUp")) return;
        var member = event.getMember();
        if (member == null) throw new NullPointerException("Error while getting member!");
        try {
            var player = Players.getPlayer(member.getIdLong());
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
