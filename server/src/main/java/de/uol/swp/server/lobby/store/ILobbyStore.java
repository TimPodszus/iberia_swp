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
     * @param lobbyId the unique code identifying the lobby
     * @return the {@code ILobby} object with the specified lobby code or null if no lobby is found
     */
    ILobby findLobby(String lobbyId);

    /**
     * Creates a new lobby with the specified name, code, users, owner, and difficulty level.
     *
     * @param lobbyId    the unique code identifying the lobby
     * @param name       the name of the lobby to create
     * @param users      the list of users who will be part of the lobby
     * @param owner      the user who owns the lobby
     * @param difficulty the difficulty level of the lobby
     * @return the newly created {@code Lobby}
     */
    ILobby createLobby(
            String lobbyId, String name, List<IUser> users, IUser owner, int difficulty
    );

    /**
     * Removes the lobby with the specified name from the system.
     *
     * @param lobbyId the id of the lobby to remove
     */
    void removeLobby(String lobbyId);

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
    ILobby saveLobby(ILobby lobby);

    /**
     * Removes all lobbies from the system.
     */
    void removeAll();
}

