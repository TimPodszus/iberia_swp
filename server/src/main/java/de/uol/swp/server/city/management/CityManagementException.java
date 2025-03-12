package de.uol.swp.server.city.management;

public class CityManagementException extends RuntimeException {
    public CityManagementException(String message) {
        super(message);
    }

    public CityManagementException(String message, Throwable cause) {
        super(message, cause);
    }
}