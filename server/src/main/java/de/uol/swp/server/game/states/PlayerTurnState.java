package de.uol.swp.server.game.states;

import de.uol.swp.common.game.action.Action;
import de.uol.swp.server.game.GameController;
import de.uol.swp.server.player.Player;

public class PlayerTurnState implements GameState {
    public void handleAction(GameController controller, Action action, Player player) {
        controller.getCurrentTurn()
                  .processAction(action);

        if (controller.getCurrentTurn()
                      .isTurnOver()) {
            controller.setState(new DrawCardState());
        }
    }
}
