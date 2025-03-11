package de.uol.swp.server.lobby.management;

import de.uol.swp.server.lobby.data.ILobby;
import de.uol.swp.server.lobby.exceptions.LobbyIsFullException;
import de.uol.swp.server.lobby.exceptions.LobbyNotFoundException;
import de.uol.swp.server.lobby.exceptions.UserAlreadyInLobbyException;
import de.uol.swp.server.usermanagement.IUser;

import java.util.List;

/**
 * Interface for managing lobbies.
 */
public interface ILobbyManagement {

    /**
     * Creates a new lobby.
     *
     * @param owner the owner of the lobby
     * @return the created lobby
     */
    ILobby createLobby(IUser owner);

    /**
     * Deletes an existing lobby.
     *
     * @param lobbyId the ID of the lobby to delete
     */
    void deleteLobby(String lobbyId);

    /**
     * Retrieves a lobby by its ID.
     *
     * @param lobbyId the ID of the lobby to retrieve
     * @return the lobby with the specified ID
     */
    ILobby getLobby(String lobbyId);

    /**
     * Retrieves a list of all lobbies.
     *
     * @return a list of all lobbies
     */
    List<ILobby> getLobbies();

    /**
     * Allows a user to join a specified lobby.
     *
     * @param lobbyId the ID of the lobby to join
     * @throws LobbyNotFoundException if the lobby, with the given id, could not be found
     * @throws LobbyIsFullException   if the lobby is already full
     * @throws UserAlreadyInLobbyException if the user is already in the lobby
     */
    void joinLobby(String lobbyId, IUser user) throws LobbyNotFoundException, LobbyIsFullException, UserAlreadyInLobbyException;

    /**
     * Updates an existing lobby.
     *
     * @param lobby the lobby to update
     * @return the updated lobby
     * @throws LobbyNotFoundException if the lobby, with the given id, could not be found
     */
    ILobby updateLobby(ILobby lobby) throws LobbyNotFoundException;

    /**
     * Removes a user from a specified lobby.
     *
     * @param lobbyId  the ID of the lobby
     * @param username the username of the user to remove
     * @return the updated lobby after the user is removed
     * @throws LobbyNotFoundException if the lobby, with the given id, could not be found
     */
    ILobby removeUser(String lobbyId, String username) throws LobbyNotFoundException;

    /**
     * Allows a user to leave a specified lobby.
     *
     * @param lobbyID the ID of the lobby to leave
     * @param user    the user who wants to leave the lobby
     */
    void leaveLobby(String lobbyID, IUser user);
}

