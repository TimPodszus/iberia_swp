package de.uol.swp.server.cards.management;

/**
 * Exception thrown when a card is not found.
 */
public class CardNotFoundException extends Exception {

    /**
     * Constructs a new CardNotFoundException with the specified detail message.
     *
     * @param message the detail message
     */
    public CardNotFoundException(String message) {
        super(message);
    }
}