package de.uol.swp.server.lobby.management;

import de.uol.swp.server.lobby.data.ILobby;
import de.uol.swp.server.lobby.store.LobbyStoreException;
import de.uol.swp.server.usermanagement.IUser;

import java.util.List;

/**
 * Interface for managing lobbies.
 */
public interface ILobbyManagement {

    /**
     * Creates a new lobby.
     *
     * @param lobbyName the name of the lobby
     * @param owner     the owner of the lobby
     * @return the created lobby
     * @throws LobbyStoreException if an error occurs during lobby creation
     */
    ILobby createLobby(String lobbyName, IUser owner) throws LobbyStoreException;

    /**
     * Deletes an existing lobby.
     *
     * @param lobbyId the ID of the lobby to delete
     * @throws LobbyStoreException if an error occurs during lobby deletion
     */
    void deleteLobby(String lobbyId) throws LobbyStoreException;

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
     * @throws LobbyStoreException if an error occurs during lobby retrieval
     */
    List<ILobby> getLobbies() throws LobbyStoreException;

    /**
     * Allows a user to join a specified lobby.
     *
     * @param lobby an Optional containing the lobby to join
     * @throws LobbyStoreException if an error occurs during the join process
     */
    void joinLobby(ILobby lobby, IUser user) throws LobbyStoreException;

    /**
     * Updates an existing lobby.
     *
     * @param lobby the lobby to update
     * @return the updated lobby
     * @throws LobbyStoreException if an error occurs during lobby update
     */
    ILobby updateLobby(ILobby lobby) throws LobbyStoreException;
}

