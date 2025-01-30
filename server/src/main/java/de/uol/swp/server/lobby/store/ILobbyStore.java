package de.uol.swp.server.lobby.store;

import de.uol.swp.server.lobby.data.ILobby;
import de.uol.swp.server.usermanagement.IUser;

import java.util.List;
import java.util.Map;


/**
 * The {@code ILobbyStore} interface defines the contract for managing lobbies in the system.
 * It provides methods for creating, updating, retrieving, removing, and saving lobbies,
 * as well as interacting with user data related to lobbies.
 */
public interface ILobbyStore {

    /**
     * Searches for a lobby by its lobby code.
     *
     * @param lobbycode the unique code identifying the lobby
     * @return the {@code ILobby} object with the specified lobby code or null if no lobby is found
     */
    ILobby findLobby(String lobbycode);

    /**
     * Creates a new lobby with the specified name, code, users, owner, and difficulty level.
     *
     * @param lobbyCode  the unique code identifying the lobby
     * @param name       the name of the lobby to create
     * @param users      the list of users who will be part of the lobby
     * @param owner      the user who owns the lobby
     * @param difficulty the difficulty level of the lobby
     * @return the newly created {@code Lobby}
     */
    ILobby createLobby(
            String lobbyCode,
            String name,
            List<IUser> users,
            IUser owner,
            int difficulty
    );

    /**
     * Updates an existing lobby with the specified name, code, users, owner, and difficulty level.
     *
     * @param name       the name of the lobby to update
     * @param lobbycode  the unique code identifying the lobby
     * @param users      the list of users who will be part of the lobby
     * @param owner      the user who owns the lobby
     * @param difficulty the difficulty level of the lobby
     * @return the updated {@code Lobby}
     */
    ILobby updateLobby(
            String name,
            String lobbycode,
            List<IUser> users,
            IUser owner,
            int difficulty
    );

    /**
     * Removes the lobby with the specified name from the system.
     *
     * @param name the name of the lobby to remove
     * @throws LobbyStoreException if an error occurs during the removal process
     */
    void removeLobby(String name) throws LobbyStoreException;

    /**
     * Retrieves all lobbies currently stored in the system.
     *
     * @return a {@code Map} containing all lobbies, where the key is the lobby name and the value is the {@code ILobby}
     * object
     */
    Map<String, ILobby> getAllLobbies();

    /**
     * Saves the specified lobby to the system.
     * This method is responsible for persisting lobby data to the database.
     *
     * @param lobby the {@code ILobby} containing the lobby information to save
     */
    void saveLobby(ILobby lobby);

    /**
     * Removes a user from the specified lobby.
     *
     * @param lobbyID the unique identifier of the lobby
     * @param user    the user to remove from the lobby
     * @throws LobbyStoreException if an error occurs during the removal process
     */
    void removeUser(String lobbyID, IUser user) throws LobbyStoreException;

    /**
     * Adds a user to the specified lobby.
     *
     * @param lobbyCode the unique code identifying the lobby
     * @param user      the user to add to the lobby
     * @throws LobbyStoreException if an error occurs during the join process
     */
    void joinUser(String lobbyCode, IUser user) throws LobbyStoreException;
}

