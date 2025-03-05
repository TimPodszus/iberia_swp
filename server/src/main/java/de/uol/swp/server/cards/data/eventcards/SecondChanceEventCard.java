package de.uol.swp.server.cards.data.eventcards;

import de.uol.swp.server.cards.events.SecondChanceEvent;

/**
 * Represents a Second Chance event card in the game.
 * This card allows the player to retrieve the city card of their current location from the discard pile.
 */
public class SecondChanceEventCard extends EventCard {
    private static final String TITLE = "Zweite Chance";
    private static final String DESCRIPTION = "Du bekommst die Stadtkarte deines momentanen Aufenthaltsort aus dem " +
            "Ablagestapel.";

    /**
     * Constructs a new SecondChanceEventCard with the specified ID.
     *
     * @param id the unique identifier of the card
     */
    public SecondChanceEventCard(int id) {
        super(id, TITLE, DESCRIPTION);
    }

    /**
     * Executes the action associated with this card.
     * Creates and posts a SecondChanceEvent to the event bus.
     *
     * @param lobbyId the ID of the lobby where the event occurs
     * @param username the username of the player executing the card
     */
    @Override
    public void execute(String lobbyId, String username) {
        SecondChanceEvent event = new SecondChanceEvent(lobbyId, username);
        bus.post(event);
    }
}