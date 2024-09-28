package de.uol.swp.common.lobby.dto;

import de.uol.swp.common.user.User;

import java.io.Serializable;
import java.util.List;

/**
 * Interface representing a Lobby Data Transfer Object (DTO).
 */
public interface ILobbyDTO extends Serializable {

    /**
     * Gets the name of the lobby.
     *
     * @return the name of the lobby
     */
    String getName();

    /**
     * Gets the owner of the lobby.
     *
     * @return the owner of the lobby
     */
    User getOwner();

    /**
     * Gets the list of users in the lobby.
     *
     * @return the list of users
     */
    List<User> getUsers();

    /**
     * Gets the lobby code.
     *
     * @return the lobby code
     */
    String getLobbyCode();

    /**
     * Gets the difficulty level of the lobby.
     *
     * @return the difficulty level
     */
    int getDifficulty();
}
