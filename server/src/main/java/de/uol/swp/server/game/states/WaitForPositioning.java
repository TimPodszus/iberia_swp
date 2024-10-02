package de.uol.swp.server.game.states;

import de.uol.swp.server.game.data.Game;
import de.uol.swp.server.player.Player;
import lombok.Getter;
import lombok.Setter;

/**
 * Represents the game state where players are setting their initial positions at the start of the game.
 * This state manages the positioning of players based on their chosen locations and ensures that all players
 * are positioned before moving to the next game phase.
 */
public class WaitForPositioning implements IGameState {
    @Getter
    @Setter
    private int positionedPlayersCount = 0;

    /**
     * Processes player actions intended to set their starting positions on the game board.
     * This method expects actions to be instances of MoveAction, specifying the destination for the player's starting position.
     * If the destination is invalid, a GameTurnException is thrown. After all players are positioned, the game state transitions
     * to the PlayerTurnState.
     *
     * @param player the player who is taking the action and attempting to set their starting position
     */
    public void handleAction(Game game, Player player) {
        //Todo: SpielInitialisierung
    }
}
