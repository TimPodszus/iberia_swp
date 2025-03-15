package de.uol.swp.server.cards.data.eventcards;

import de.uol.swp.server.cards.events.ForTheGoodCauseEvent;

/**
 * For the good cause event card
 * The player may choose an event card from the discard pile
 */
public class ForTheGoodCauseEventCard extends EventCard {
    private static final String TITLE = "Für den guten Zweck";
    private static final String DESCRIPTION = "Du darfst dir eine Ereigniskarte aus dem Ablagestapel aussuchen";

    /**
     * Creates a new ForTheGoodCauseEventCard, with the given id
     * @param id the id of the card
     */
    public ForTheGoodCauseEventCard(int id) {
        super(id, TITLE, DESCRIPTION);
    }

    @Override
    public void execute(String lobbyId, String username) {
        ForTheGoodCauseEvent event = new ForTheGoodCauseEvent(lobbyId, username);
        bus.post(event);
    }
}
