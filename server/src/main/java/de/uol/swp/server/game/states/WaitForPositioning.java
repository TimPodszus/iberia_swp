package de.uol.swp.server.game.states;

import de.uol.swp.common.game.action.Action;
import de.uol.swp.common.game.action.MoveAction;
import de.uol.swp.server.game.GameController;
import de.uol.swp.server.game.GameTurn;
import de.uol.swp.server.player.Player;

public class WaitForPositioning implements GameState {
    private int positionedPlayersCount = 0;

    public void handleAction(GameController controller, Action action, Player player) {
        if (action instanceof MoveAction moveAction) {
            try {
                player.setStartingPosition(moveAction.getDestination()
                                                     .getName());
                positionedPlayersCount++;
            } catch (Exception e) {
                // Keine Valide Stadt Message an den User
            }
        }
        if (positionedPlayersCount == controller.getPlayers()
                                                .size()) {
            controller.setCurrentTurn(new GameTurn(controller.getPlayers()
                                                             .get(controller.getCurrentPlayerIndex()),
                    controller.getBoard()
            ));
            controller.setState(new PlayerTurnState());
        }
    }
}
