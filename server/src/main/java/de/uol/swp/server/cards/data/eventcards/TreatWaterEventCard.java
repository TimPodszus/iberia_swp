package de.uol.swp.server.cards.data.eventcards;

import de.uol.swp.server.cards.events.TreatWaterEvent;

/**
 * Represents an event card that triggers the TreatWaterEvent.
 */
public class TreatWaterEventCard extends EventCard {
    private static final String TITLE = "Wasser aufbereiten";
    private static final String DESCRIPTION = "Platziere bis zu 2 Wasseraufbereitungsmarker in 1 oder 2 " +
            "unterschiedlichen Region(en).";

    /**
     * Constructs a new TreatWaterEventCard.
     *
     * @param id the unique identifier of the event card
     */
    public TreatWaterEventCard(int id) {
        super(id, TITLE, DESCRIPTION);
    }

    /**
     * Executes the event by posting a TreatWaterEvent to the event bus.
     *
     * @param lobbyId  the ID of the lobby where the event occurs
     * @param username the username of the player involved in the event
     */
    @Override
    public void execute(String lobbyId, String username) {
        TreatWaterEvent event = new TreatWaterEvent(lobbyId, username);
        bus.post(event);
    }
}
