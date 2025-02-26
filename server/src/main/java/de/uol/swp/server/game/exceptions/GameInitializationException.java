package de.uol.swp.server.game.exceptions;

/**
 * Exception thrown when there is an error during game initialization.
 */
public class GameInitializationException extends Exception {

    /**
     * Constructs a new GameInitializationException with the specified detail message and cause.
     *
     * @param message the detail message
     * @param cause the cause of the exception
     */
    public GameInitializationException(String message, Throwable cause) {
        super(message, cause);
    }
}