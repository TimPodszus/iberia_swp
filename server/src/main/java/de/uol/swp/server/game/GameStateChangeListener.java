package de.uol.swp.server.game;

import de.uol.swp.server.game.data.IGame;

public interface GameStateChangeListener {
    void onGameStateChange(IGame game);
}