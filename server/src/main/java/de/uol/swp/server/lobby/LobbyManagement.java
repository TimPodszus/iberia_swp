package de.uol.swp.server.lobby;

import de.uol.swp.common.lobby.ILobby;
import de.uol.swp.common.user.UserDTO;
import de.uol.swp.server.lobby.Lobby;
import de.uol.swp.common.lobby.dto.LobbyDTO;
import de.uol.swp.common.user.User;
import lombok.Getter;

import java.util.*;

/**
 * Manages creation, deletion and storing of lobbies
 *
 * @author Marco Grawunder
 * @see de.uol.swp.server.lobby.Lobby
 * @see de.uol.swp.common.lobby.dto.LobbyDTO
 * @since 2019-10-08
 */
@Getter
public class LobbyManagement {

    private final Map<String, ILobby> lobbies = Map.of(
            "test1",
            new Lobby("test1", "testcode", new ArrayList<>(), new UserDTO("test1", "test1"), 4)
    );

    /**
     * Creates a new lobby and adds it to the list
     *
     * @param name  the name of the lobby to create
     * @param owner the user who wants to create a lobby
     * @throws IllegalArgumentException name already taken
     * @implNote the primary key of the lobbies is the name therefore the name has
     * to be unique
     * @see de.uol.swp.common.user.User
     * @since 2019-10-08
     */
    public void createLobby(String name, User owner) {
        if (lobbies.containsKey(name)) {
            throw new IllegalArgumentException("Lobby name " + name + " already exists!");
        }
        lobbies.put(name, new Lobby(name, "testcode", new ArrayList<>(), owner, 4));
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
    public Optional<ILobby> getLobby(String name) {
        ILobby lobby = lobbies.get(name);
        if (lobby != null) {
            return Optional.of(lobby);
        }
        return Optional.empty();
    }

}
