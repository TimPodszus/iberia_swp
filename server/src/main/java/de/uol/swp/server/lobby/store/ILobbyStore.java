package de.uol.swp.server.lobby.store;

import de.uol.swp.common.user.User;
import de.uol.swp.server.lobby.data.ILobby;

import java.sql.SQLException;
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
     * @return an {@code Optional} containing the lobby if found, or an empty {@code Optional} if no lobby is found
     */
    ILobby findLobby(String lobbycode) throws SQLException;

    /**
     * Creates a new lobby with the specified name, code, users, owner, and difficulty level.
     *
     * @param name       the name of the lobby to create
     * @param lobbycode  the unique code identifying the lobby
     * @param users      the list of users who will be part of the lobby
     * @param owner      the user who owns the lobby
     * @param difficulty the difficulty level of the lobby
     * @return the newly created {@code Lobby}
     * @throws SQLException if an error occurs while saving the lobby to the database
     */
    ILobby createLobby(String name, String lobbycode, List<User> users, User owner, int difficulty) throws SQLException;

    /**
     * Updates an existing lobby with the specified name, code, users, owner, and difficulty level.
     *
     * @param name       the name of the lobby to update
     * @param lobbycode  the unique code identifying the lobby
     * @param users      the list of users who will be part of the lobby
     * @param owner      the user who owns the lobby
     * @param difficulty the difficulty level of the lobby
     * @return the updated {@code Lobby}
     * @throws SQLException if an error occurs while updating the lobby in the database
     */
    ILobby updateLobby(String name, String lobbycode, List<User> users, User owner, int difficulty) throws SQLException;

    /**
     * Removes the lobby with the specified name from the system.
     *
     * @param name the name of the lobby to remove
     */
    void removeLobby(String name);

    /**
     * Retrieves all lobbies currently stored in the system.
     *
     * @return a {@code Map} containing all lobbies, where the key is the lobby name and the value is the {@code ILobby}
     * object
     */
    Map<String, ILobby> getAllLobbies() throws SQLException;

    /**
     * Saves the specified lobby to the system.
     * This method is responsible for persisting lobby data to the database.
     *
     * @param lobby the {@code ILobby} containing the lobby information to save
     * @throws SQLException if an error occurs while saving the lobby to the database
     */
    void saveLobby(ILobby lobby) throws SQLException;
}

