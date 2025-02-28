package de.uol.swp.server.usermanagement.exceptions;

/**
 * This exception is thrown when a session is not found.
 */
public class SessionNotFoundException extends RuntimeException {
    private static final String DEFAULT_MESSAGE = "Session not found.";

    public SessionNotFoundException() {
        super(DEFAULT_MESSAGE);
    }

    public SessionNotFoundException(String message) {
        super(message);
    }
}
