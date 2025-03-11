package de.uol.swp.server.cards.data.eventcards;

import de.uol.swp.server.cards.events.HospitalFoundationEvent;

public class HospitalFoundationEventCard extends EventCard {
    private static final String TITLE = "Krankenhausgründung";
    private static final String DESCRIPTION = "Baue oder verschiebe ein Krankenhaus in eine gleichfarbige Stadt.";

    /**
     * Constructs a new EventCard with the specified id and title.
     *
     * @param id the unique identifier of the card
     */
    public HospitalFoundationEventCard(int id) {
        super(id, TITLE, DESCRIPTION);
    }

    @Override
    public void execute(String lobbyId, String username) {
        HospitalFoundationEvent event = new HospitalFoundationEvent(lobbyId, username);
        bus.post(event);
    }
}
