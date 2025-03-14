package de.uol.swp.server.cards.data.eventcards;

import de.uol.swp.server.cards.events.ExchangeOfLettersEvent;

public class ExchangeOfLettersEventCard extends EventCard {

    private static final String TITLE = "Briefwechsel";
    private static final String DESCRIPTION = "Tausche eine Handkarten mit einem anderen Spieler.";

    /**
     * Constructs a new ExchangeOfLetters event card.
     *
     * @param id the unique identifier of the event card
     */
    public ExchangeOfLettersEventCard(int id) {
        super(id, TITLE, DESCRIPTION);
    }

    /**
     * Executes the event by posting an ExchangeOfLettersEvent to the event bus.
     *
     * @param lobbyId  the ID of the lobby where the event occurs
     * @param username the username of the player involved in the event
     */
    @Override
    public void execute(String lobbyId, String username) {
        ExchangeOfLettersEvent event = new ExchangeOfLettersEvent(lobbyId, username);
        bus.post(event);
    }
}
