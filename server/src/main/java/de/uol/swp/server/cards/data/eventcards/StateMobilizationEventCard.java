package de.uol.swp.server.cards.data.eventcards;

import de.uol.swp.server.cards.events.StateMobilizationEvent;
import lombok.Getter;
import lombok.Setter;

/**
 * Represents an event card that triggers the StateMobilizationEvent.
 * This event card allows all players to move once.
 */
@Getter
public class StateMobilizationEventCard extends EventCard{
    private static final String TITLE = "Staatsmobilisierung";
    private static final String DESCRIPTION = "Alle Spieler dürfen sich einmal bewegen.";

    @Setter
    private int playersToMove = 0;

    public StateMobilizationEventCard(int id) {
        super(id, TITLE, DESCRIPTION);
    }

    @Override
    public void execute(String lobbyId, String username) {
        StateMobilizationEvent event = new StateMobilizationEvent(lobbyId);
        bus.post(event);
    }
}
