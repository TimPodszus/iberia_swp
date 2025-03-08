package de.uol.swp.server.cards.management;

/**
 * Exception thrown when a card is not playable.
 * This is a runtime exception that indicates the card cannot be played.
 */
public class CardNotPlayableException extends Exception {
    private static final String MESSAGE = "The card is not playable.";

    /**
     * Constructs a new CardNotPlayableException with a default message.
     */
    public CardNotPlayableException() {
        super(MESSAGE);
    }
}