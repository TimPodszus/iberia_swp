package de.uol.swp.server.lobby;

import de.uol.swp.common.lobby.Lobby;
import de.uol.swp.common.lobby.dto.LobbyDTO;
import de.uol.swp.common.user.User;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Manages creation, deletion and storing of lobbies
 *
 * @see de.uol.swp.common.lobby.Lobby
 * @see de.uol.swp.common.lobby.dto.LobbyDTO
 * @author Marco Grawunder
 * @since 2019-10-08
 */
public class LobbyManagement {

    private final Map<String, Lobby> lobbies = new HashMap<>();

    /**
     * Creates a new lobby and adds it to the list
     *
     * @implNote the primary key of the lobbies is the name therefore the name has
     *           to be unique
     * @param name the name of the lobby to create
     * @param owner the user who wants to create a lobby
     * @see de.uol.swp.common.user.User
     * @throws IllegalArgumentException name already taken
     * @since 2019-10-08
     */
    public void createLobby(String name, User owner) {
        if (lobbies.containsKey(name)) {
            throw new IllegalArgumentException("Lobby name " + name + " already exists!");
        }
        lobbies.put(name, new LobbyDTO(name, owner));
    }

    /**
     * Deletes lobby with requested name
     *
     * @param name String containing the name of the lobby to delete
     * @throws IllegalArgumentException there exists no lobby with the  requested
     *                                  name
     * @since 2019-10-08
     */
    public void dropLobby(String name) {
        if (!lobbies.containsKey(name)) {
            throw new IllegalArgumentException("Lobby name " + name + " not found!");
        }
        lobbies.remove(name);
    }

    /**
     * Searches for the lobby with the requested name
     *
     * @param name String containing the name of the lobby to search for
     * @return either empty Optional or Optional containing the lobby
     * @see Optional
     * @since 2019-10-08
     */
    public Optional<Lobby> getLobby(String name) {
        Lobby lobby = lobbies.get(name);
        if (lobby != null) {
            return Optional.of(lobby);
        }
        return Optional.empty();
    }


}
