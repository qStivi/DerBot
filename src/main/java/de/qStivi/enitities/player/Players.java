package de.qStivi.enitities.player;

import de.qStivi.Bot;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class Players {

    private final static Map<Long, Player> PLAYER_MAP = new HashMap<>();

    public static Player getPlayer(long id) throws SQLException {
        var player = PLAYER_MAP.get(id);
        if (player == null) {
            var user = Bot.JDA.getUserById(id);
            if (user == null) {
                throw new NullPointerException("Error while getting User!");
            }
            player = new Player(user.getName(), id);
            PLAYER_MAP.put(id, player);
        }
        return player;
    }

}
