package de.uol.swp.server.lobby;

import de.uol.swp.common.lobby.ILobby;
import de.uol.swp.common.lobby.dto.LobbyDTO;
import de.uol.swp.common.user.User;
import de.uol.swp.server.lobby.store.LobbyStore;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
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
    public Lobby createLobby(String name, User owner) throws LobbyManagementException, SQLException {
        if (lobbyStore.findLobby(name) == null) {
            throw new LobbyManagementException("Lobby name " + name + " already exists!");
        }
        String lobbyCode = generateLobbyCode();
        List<User> users = new ArrayList<>();
        users.add(owner);
        try {
            LobbyDTO lobbyDTO = new LobbyDTO(name, owner, lobbyCode, 4);
            lobbyStore.saveLobby(lobbyDTO);
        } catch (SQLException e) {
            throw new LobbyManagementException("Failed to save lobby to the database");
        }
        return new Lobby(name, lobbyCode, users, owner, 4);
    }

    /**
     * Generates a unique lobby code.
     *
     * @return a unique lobby code
     */
    private String generateLobbyCode() throws SQLException {
        final String[] code = new String[1];
        do {
            code[0] = UUID.randomUUID()
                    .toString()
                    .substring(0, 8);
        } while (lobbyStore.getAllLobbies().values()
                .stream()
                .anyMatch(lobby -> lobby.getLobbyCode()
                        .equals(code[0])));
        return code[0];
    }

    /**
     * Deletes lobby with requested lobbycode
     *
     * @param lobbycode String containing the name of the lobby to delete
     * @throws IllegalArgumentException there exists no lobby with the  requested
     *                                  name
     * @since 2019-10-08
     */

    public void dropLobby(String lobbycode) throws SQLException, LobbyManagementException {
        if (lobbyStore.findLobby(lobbycode) == null) {
            throw new LobbyManagementException("Lobbycode " + lobbycode + " not found!");
        }
        lobbyStore.removeLobby(lobbycode);
    }

    /**
     * Searches for the lobby with the requested lobbycode
     *
     * @param lobbycode String containing the lobbycode of the lobby to search for
     * @return either empty Optional or Optional containing the lobby
     * @see Optional
     * @since 2019-10-08
     */
    public Optional<Lobby> getLobby(String lobbycode) throws SQLException {
        Lobby lobby = lobbyStore.findLobby(lobbycode);
        if (lobby != null) {
            return Optional.of(lobby);
        }
        return Optional.empty();
    }
}