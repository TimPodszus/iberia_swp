package de.uol.swp.server.game.states;

import de.uol.swp.common.game.action.Action;
import de.uol.swp.server.game.GameController;
import de.uol.swp.server.player.Player;

public interface GameState {
    void handleAction(GameController controller, Action action, Player player);
}



