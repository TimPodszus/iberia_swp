package de.uol.swp.server.lobby.data;

import de.uol.swp.common.lobby.dto.LobbyDTO;
import de.uol.swp.common.user.User;

import java.io.Serializable;
import java.util.List;

/**
 * Interface to unify lobby objects
 * This is an Interface to allow for multiple types of lobby objects since it is
 * possible that not every client has to have every information of the lobby.
 *
 * @author Marco Grawunder
 * @see LobbyDTO
 * @since 2019-10-08
 */
public interface ILobby extends Serializable {

    /**
     * Getter for the lobby's id
     *
     * @return A String containing the id of the lobby
     */
    String getLobbyCode();

    /**
     * Getter for the lobby's name
     *
     * @return A String containing the name of the lobby
     * @since 2019-10-08
     */
    String getName();

    /**
     * Getter for all users in the lobby
     *
     * @return A Set containing all user in this lobby
     * @since 2019-10-08
     */
    List<User> getUsers();

    /**
     * Getter for the current owner of the lobby
     *
     * @return A User object containing the owner of the lobby
     * @since 2019-10-08
     */
    User getOwner();

    /**
     * Getter for the lobby's difficulty level
     *
     * @return An integer representing the difficulty level of the lobby
     */
    int getDifficulty();

    /**
     * Changes the owner of the lobby
     *
     * @param user The user who should be the new owner
     * @since 2019-10-08
     */
    void updateOwner(User user);

    /**
     * Adds a new user to the lobby
     *
     * @param user The new user to add to the lobby
     * @since 2019-10-08
     */
    void joinUser(User user);

    /**
     * Removes an user from the lobby
     *
     * @param user The user to remove from the lobby
     * @since 2019-10-08
     */
    void leaveUser(User user);
}
