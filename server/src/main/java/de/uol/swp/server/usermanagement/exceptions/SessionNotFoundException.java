package de.uol.swp.server.usermanagement.exceptions;

/**
 * This exception is thrown when a session is not found.
 */
public class SessionNotFoundException extends RuntimeException {

    public SessionNotFoundException(String message) {
        super(message);
    }
}
