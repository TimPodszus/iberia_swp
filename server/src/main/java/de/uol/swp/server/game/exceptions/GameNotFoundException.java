package de.uol.swp.server.game.exceptions;

/**
 * This exception is thrown when a game is not found.
 * It is an unchecked exception.
 */
public class GameNotFoundException extends RuntimeException {

    public GameNotFoundException(String message) {
        super(message);
    }
}
