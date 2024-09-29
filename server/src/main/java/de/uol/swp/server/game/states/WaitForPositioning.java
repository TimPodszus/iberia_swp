package de.uol.swp.server.game.states;

import de.uol.swp.common.game.Action;
import de.uol.swp.common.game.MoveAction;
import de.uol.swp.server.game.GameController;
import de.uol.swp.server.game.GameTurn;
import de.uol.swp.server.player.Player;

/**
 * Represents the game state where players are setting their initial positions at the start of the game.
 * This state manages the positioning of players based on their chosen locations and ensures that all players
 * are positioned before moving to the next game phase.
 */
public class WaitForPositioning implements IGameState {
    private int positionedPlayersCount = 0;

    /**
     * Processes player actions intended to set their starting positions on the game board.
     * This method expects actions to be instances of MoveAction, specifying the destination for the player's starting position.
     * If the destination is invalid, a GameTurnException is thrown. After all players are positioned, the game state transitions
     * to the PlayerTurnState.
     *
     * @param controller the game controller that manages state transitions and other game interactions
     * @param action the action performed by the player, expected to be a MoveAction specifying the destination
     * @param player the player who is taking the action and attempting to set their starting position
     */
    public void handleAction(GameController controller, Action action, Player player) {
        if (action instanceof MoveAction moveAction) {
            try {
                player.setStartingPosition(moveAction.getDestination().getName());
                positionedPlayersCount++;
            } catch (Exception e) {
                // StatusResponse
            }
        }
        if (positionedPlayersCount == controller.getPlayers().size()) {
            controller.setCurrentTurn(new GameTurn(controller.getPlayers().get(controller.getCurrentPlayerIndex()),
                    controller.getBoard()));
            controller.setState(new PlayerTurnState());
        }
    }
}
