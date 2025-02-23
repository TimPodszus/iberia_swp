package de.uol.swp.server.cards.data.eventcards;

import de.uol.swp.server.cards.events.MovePlayerAnywhereEvent;

/**
 * Represents an event card that triggers the MovePlayerAnywhereEvent.
 */
public class OnTheMoveDayAndNightEventCard extends EventCard {
    private static final String TITLE = "Tag und Nacht unterwegs";
    private static final String DESCRIPTION = "Du darfst dich auf dem Spielplan frei bewegen.";

    /**
     * Constructs a new OnTheMoveDayAndNightEventCard.
     *
     * @param id the unique identifier of the event card
     */
    public OnTheMoveDayAndNightEventCard(int id) {
        super(id, TITLE, DESCRIPTION);
    }

    /**
     * Executes the event by posting a MovePlayerAnywhereEvent to the event bus.
     *
     * @param lobbyId  the ID of the lobby where the event occurs
     * @param username the username of the player involved in the event
     */
    @Override
    public void execute(String lobbyId, String username) {
        MovePlayerAnywhereEvent event = new MovePlayerAnywhereEvent(lobbyId, username);
        bus.post(event);
    }
}
