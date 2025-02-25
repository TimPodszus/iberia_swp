package de.uol.swp.server.game.states;

import de.uol.swp.common.game.StateType;
import de.uol.swp.server.cards.data.eventcards.EventCard;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * The state of an event card.
 */
@Getter
@AllArgsConstructor
public class EventState implements IGameState {

    private final EventCard eventCard;

    public StateType getStateType() {
        return StateType.EVENT_STATE;
    }
}
