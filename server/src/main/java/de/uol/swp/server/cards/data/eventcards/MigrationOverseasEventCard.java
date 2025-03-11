package de.uol.swp.server.cards.data.eventcards;

import de.uol.swp.server.cards.events.MigrationOverseasEvent;

public class MigrationOverseasEventCard extends EventCard {
    private static final String TITLE = "Migration nach Übersee";
    private static final String DESCRIPTION = "Der aktuelle Spieler darf bis zu zwei Seuchenwürfel entfernen.";

    /**
     * Constructs a new MigrationOverseasEventCard.
     *
     * @param id the unique identifier of the event card
     */
    public MigrationOverseasEventCard(int id) {
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
        MigrationOverseasEvent event = new MigrationOverseasEvent(lobbyId, username);
        bus.post(event);
    }
}
