package de.uol.swp.server.lobby;

import de.uol.swp.common.lobby.ILobby;
import de.uol.swp.common.lobby.dto.LobbyDTO;
import de.uol.swp.common.user.User;
import de.uol.swp.server.lobby.store.LobbyStore;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/**
 * Manages creation, deletion and storing of lobbies
 *
 * @author Marco Grawunder
 * @see ILobby
 * @see LobbyDTO
 * @since 2019-10-08
 */
public class LobbyManagement {
    private final Map<String, Lobby> lobbies = new HashMap<>();
    private final LobbyStore lobbyStore = new LobbyStore();

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
    public void createLobby(String name, User owner) throws LobbyManagementException {
        if (lobbies.containsKey(name)) {
            throw new LobbyManagementException("Lobby name " + name + " already exists!");
        }
        String lobbyCode = generateLobbyCode();
        List<User> users = new ArrayList<>();
        users.add(owner);
        Lobby newLobby = new Lobby(name, lobbyCode, users, owner, 4);
        lobbies.put(name, newLobby);
        try {
            LobbyDTO lobbyDTO = new LobbyDTO(name, owner, lobbyCode, 4);
            lobbyStore.saveLobby(lobbyDTO);
        } catch (SQLException e) {
            throw new LobbyManagementException("Failed to save lobby to the database");
        }
    }

    /**
     * Generates a unique lobby code.
     *
     * @return a unique lobby code
     */
    private String generateLobbyCode() {
        final String[] code = new String[1];
        do {
            code[0] = UUID.randomUUID()
                    .toString()
                    .substring(0, 8);
        } while (lobbies.values()
                .stream()
                .anyMatch(lobby -> lobby.getLobbyCode()
                        .equals(code[0])));
        return code[0];
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
        lobbyStore.removeLobby(name);
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