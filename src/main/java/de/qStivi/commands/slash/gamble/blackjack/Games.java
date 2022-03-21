package de.qStivi.commands.slash.gamble.blackjack;

import java.util.HashMap;
import java.util.Map;

public class Games {

    private static final Map<Long, Game> GAME_MAP = new HashMap<>();

    public static Game getGameByPlayerId(long id) {
        return GAME_MAP.get(id);
    }

    public static boolean putGameByPlayerId(long id, Game game) {
        if (GAME_MAP.get(id) != null) return false;
        GAME_MAP.put(id, game);
        return true;
    }
}
