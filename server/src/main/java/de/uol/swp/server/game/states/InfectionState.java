package de.uol.swp.server.game.states;

import de.uol.swp.common.game.action.Action;
import de.uol.swp.server.game.GameController;
import de.uol.swp.server.game.GameTurn;
import de.uol.swp.server.player.Player;

/**
 * Represents the state in a game where cities are infected.
 * This state manages the infection process during a game, specifically managing the
 * distribution of infection across cities based on drawn infection cards.
 */
public class InfectionState implements GameState {
    private int infectedCities = 0;

    /**
     * Handles the action of infecting cities during a game turn.
     * Each call to this method results in a city being infected based on an infection card drawn.
     * The state counts the number of infected cities and, once it matches the infection counter,
     * transitions the game to the next player's turn and updates the game state accordingly.
     *
     * @param controller the game controller that manages state transitions and game interactions
     * @param action     the action that triggered this method, typically involving drawing an infection card
     * @param player     the player whose turn triggered the infection process
     */
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
