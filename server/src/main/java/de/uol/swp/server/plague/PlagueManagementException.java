package de.uol.swp.server.plague;

/**
 * A custom exception class for handling plague management errors.
 * This exception is thrown when an error occurs related to the management of plagues,
 * such as issues with plague research, handling, or other plague-related operations.
 */
public class PlagueManagementException extends Exception {
    public PlagueManagementException(String message) {
        super(message);
    }
}
