package de.uol.swp.server.game.states;

import de.uol.swp.common.game.StateType;
import lombok.Getter;
import lombok.Setter;

/**
 * Represents the state in a game where players draw cards.
 * This state is responsible for managing the action of drawing player cards during a game turn.
 * It ensures that each player draws two cards and then transitions the game state to InfectionState,
 * provided the player holds seven or fewer cards.
 */
@Getter
@Setter
public class DrawCardState implements IGameState {
    private int cardsDrawn = 0;

    public StateType getStateType() {
        return StateType.DRAW_CARD_STATE;
    }
}
