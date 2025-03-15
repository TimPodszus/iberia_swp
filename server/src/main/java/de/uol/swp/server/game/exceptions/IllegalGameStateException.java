package de.uol.swp.server.game.exceptions;

/**
 * This exception is thrown when a move is done which is not allowed in the current game state.
 */
public class IllegalGameStateException extends Exception{

    public IllegalGameStateException(String message) {
        super(message);
    }
}
