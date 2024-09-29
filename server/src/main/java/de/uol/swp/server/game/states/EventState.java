package de.uol.swp.server.game.states;

import de.uol.swp.common.game.Action;
import de.uol.swp.server.game.GameController;
import de.uol.swp.server.player.Player;

public class EventState implements IGameState {
    public void handleAction(GameController controller, Action action, Player player) {
        //Event ausführen

        controller.setState(controller.getPreviousState());
    }
}
