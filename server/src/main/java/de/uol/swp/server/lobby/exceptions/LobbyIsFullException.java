package de.uol.swp.server.lobby.exceptions;

public class LobbyIsFullException extends Exception {
    public LobbyIsFullException(String message) {
        super(message);
    }
}
