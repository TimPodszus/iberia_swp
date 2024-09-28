package de.uol.swp.server.lobby.management;

import de.uol.swp.common.user.User;
import de.uol.swp.server.lobby.data.ILobby;

import java.util.Optional;

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
     * @throws LobbyManagementException if an error occurs during lobby creation
     */
    ILobby createLobby(String lobbyName, User owner) throws LobbyManagementException;

    /**
     * Deletes an existing lobby.
     *
     * @param lobbyId the ID of the lobby to delete
     * @throws LobbyManagementException if an error occurs during lobby deletion
     */
    void deleteLobby(String lobbyId) throws LobbyManagementException;

    /**
     * Retrieves a lobby by its ID.
     *
     * @param lobbyId the ID of the lobby to retrieve
     * @return an Optional containing the lobby if found, or an empty Optional if not found
     * @throws LobbyManagementException if an error occurs during lobby retrieval
     */
    Optional<ILobby> getLobby(String lobbyId) throws LobbyManagementException;
}