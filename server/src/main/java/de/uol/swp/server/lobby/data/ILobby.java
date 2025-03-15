package de.uol.swp.server.lobby.data;

import de.uol.swp.common.lobby.dto.LobbyDTO;

import de.uol.swp.server.usermanagement.IUser;


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
    String getLobbyId();

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
    List<IUser> getUsers();

    /**
     * Getter for a user in the lobby by username
     *
     * @param username The username of the user to retrieve
     * @return The user with the specified username, or null if not found
     */
    IUser getUser(String username);

    /**
     * Getter for the current owner of the lobby
     *
     * @return A User object containing the owner of the lobby
     * @since 2019-10-08
     */
    IUser getOwner();

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
    void updateOwner(IUser user);

    /**
     * Adds a new user to the lobby
     *
     * @param user The new user to add to the lobby
     * @since 2019-10-08
     */
    void addUser(IUser user);

    /**
     * Removes an user from the lobby
     *
     * @param user The user to remove from the lobby
     * @since 2019-10-08
     */
    void removeUser(IUser user);

    /**
     * Checks if this lobby is equal to another object.
     *
     * @param o The object to compare with this lobby
     * @return true if the specified object is equal to this lobby, false otherwise
     */
    boolean equals(Object o);

    /**
     * Returns a hash code value for the object.
     *
     * @return an integer hash code value
     */
    int hashCode();

    /**
     * Getter for the maximum number of users allowed in the lobby.
     *
     * @return An integer representing the maximum number of users.
     */
    int getMaxUsers();
}
