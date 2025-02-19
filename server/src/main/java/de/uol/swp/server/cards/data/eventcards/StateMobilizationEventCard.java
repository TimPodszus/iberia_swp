package de.uol.swp.server.cards.data.eventcards;

import de.uol.swp.server.cards.events.StateMobilizationEvent;
import de.uol.swp.server.player.data.IPlayer;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents an event card that triggers the StateMobilizationEvent.
 * This event card allows all players to move once.
 */
@Getter
public class StateMobilizationEventCard extends EventCard{
    private static final String TITLE = "Staatsmobilisierung";
    private static final String DESCRIPTION = "Alle Spieler dürfen sich einmal bewegen.";

    @Setter
    private List<IPlayer> playersToMove = new ArrayList<>();

    public StateMobilizationEventCard(int id) {
        super(id, TITLE, DESCRIPTION);
    }

    @Override
    public void execute(String lobbyId, String username) {
        StateMobilizationEvent event = new StateMobilizationEvent(lobbyId);
        bus.post(event);
    }

    /**
     * Removes the player from the list of players that still need to move.
     *
     * @param player the player that moved
     */
    public void playerMoved(IPlayer player) {
        playersToMove.remove(player);
    }
}
