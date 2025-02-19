package de.uol.swp.server.cards.data.eventcards;

import de.uol.swp.server.cards.events.AnotherDayEvent;

public class AnotherDayEventCard extends EventCard {
    private static final String TITLE = "Ein weiterer Tag";
    private static final String DESCRIPTION = "Der aktuelle Spieler darf zwei weitere Züge machen.";

    /**
     * Constructs a new AnotherDayEventCard.
     *
     * @param id the unique identifier of the event card
     */
    public AnotherDayEventCard(int id) {
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
        AnotherDayEvent event = new AnotherDayEvent(lobbyId, username);
        bus.post(event);
    }
}
