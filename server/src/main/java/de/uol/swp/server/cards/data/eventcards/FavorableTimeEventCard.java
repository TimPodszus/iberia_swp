package de.uol.swp.server.cards.data.eventcards;

import de.uol.swp.server.cards.events.FavorableTimeEvent;

public class FavorableTimeEventCard extends EventCard {
    private static final String TITLE = "Günstige Zeit";
    private static final String DESCRIPTION = "Zieh in der nächsten Infektionsphase nur 1 Infektionskarte, und zwar die unterste Karte des Infektionsstapels.";

    /**
     * Constructs a new FavorableTimeEventCard.
     *
     * @param id the unique identifier of the event card
     */
    public FavorableTimeEventCard(int id) {
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
        FavorableTimeEvent event = new FavorableTimeEvent(lobbyId, username);
        bus.post(event);
    }
}
