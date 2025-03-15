package de.uol.swp.server.game.states;

import de.uol.swp.common.game.StateType;
import de.uol.swp.server.game.data.IGame;
import lombok.Getter;

/**
 * Represents the state in a game where players draw cards.
 * This state is responsible for managing the action of drawing player cards during a game turn.
 * It ensures that each player draws two cards and then transitions the game state to InfectionState,
 * provided the player holds seven or fewer cards.
 */
@Getter
public class DrawCardState implements IGameState {
    public static final int MAX_CARDS_DRAWABLE = 2;
    private int cardsDrawn = 0;
    public StateType getStateType() {
        return StateType.DRAW_CARD_STATE;
    }

    public void increaseCardsDrawn(IGame game) {
        cardsDrawn++;
        if (cardsDrawn == MAX_CARDS_DRAWABLE) {
            game.setState(new InfectionState());
        }
    }
}
