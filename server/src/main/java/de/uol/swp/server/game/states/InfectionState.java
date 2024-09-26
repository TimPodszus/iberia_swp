package de.uol.swp.server.game.states;

import de.uol.swp.common.game.action.Action;
import de.uol.swp.server.game.GameController;
import de.uol.swp.server.game.GameTurn;
import de.uol.swp.server.player.Player;

public class InfectionState implements GameState {
    private int infectedCities = 0;

    public void handleAction(GameController controller, Action action, Player player) {
        int infectionCounter = controller.getBoard()
                                         .getInfectionCounter();
        GameTurn currentTurn = controller.getCurrentTurn();

        currentTurn.infectCity(currentTurn.drawInfectionCard(), 1);
        infectedCities++;

        if (infectedCities == infectionCounter) {

            controller.setCurrentTurn(new GameTurn(controller.getPlayers()
                                                             .get(controller.getCurrentPlayerIndex()),
                    controller.getBoard()
            ));
            controller.setState(new PlayerTurnState());
        }
    }
}
