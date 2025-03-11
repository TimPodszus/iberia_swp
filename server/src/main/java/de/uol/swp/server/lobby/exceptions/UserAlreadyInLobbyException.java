package de.uol.swp.server.lobby.exceptions;

public class UserAlreadyInLobbyException extends Exception {
    public UserAlreadyInLobbyException(String message) {
        super(message);
    }
}
