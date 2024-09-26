package de.uol.swp.server.lobby.management;

import de.uol.swp.common.lobby.dto.LobbyDTO;
import de.uol.swp.common.user.User;
import de.uol.swp.server.lobby.data.ILobby;
import de.uol.swp.server.lobby.data.Lobby;
import de.uol.swp.server.lobby.store.ILobbyStore;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Manages creation, deletion and storing of lobbies
 *
 * @author Marco Grawunder
 * @see Lobby
 * @see de.uol.swp.common.lobby.dto.LobbyDTO
 * @see Lobby
 * @see LobbyDTO
 * @since 2019-10-08
 */
public class LobbyManagement implements ILobbyManagement {
    private final ILobbyStore lobbyStore;

    /**
     * Constructs a new LobbyManagement instance and initializes the lobby store.
     */
    public LobbyManagement(ILobbyStore lobbyStore) {
        this.lobbyStore = lobbyStore;
    }

    public ILobby createLobby(String name, User owner) throws LobbyManagementException {
        try {
            if (lobbyStore.findLobby(name) != null) {
                throw new LobbyManagementException("Lobby name " + name + " already exists!");
            }
            String lobbyID = generateLobbyID();
            List<User> users = new ArrayList<>();
            users.add(owner);
            ILobby lobby = lobbyStore.createLobby(name, lobbyID, users, owner, 4);
            saveLobby(lobby);
            return lobby;
        } catch (SQLException e) {
            throw new LobbyManagementException("Failed to create lobby");
        }
    }

    public void deleteLobby(String lobbyId) throws LobbyManagementException {
        try {
            if (lobbyStore.findLobby(lobbyId) == null) {
                throw new LobbyManagementException("LobbyID " + lobbyId + " not found!");
            }
            lobbyStore.removeLobby(lobbyId);
        } catch (SQLException e) {
            throw new LobbyManagementException("Failed to delete lobby");
        }
    }

    public Optional<ILobby> getLobby(String lobbyID) throws LobbyManagementException {
        try {
            ILobby lobby = lobbyStore.findLobby(lobbyID);
            if (lobby != null) {
                return Optional.of(lobby);
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new LobbyManagementException("Failed to get lobby");
        }
    }

    /**
     * Saves the given lobby to the lobby store.
     *
     * @param lobby the lobby to be saved
     * @throws LobbyManagementException if there is an error saving the lobby
     */
    private void saveLobby(ILobby lobby) throws LobbyManagementException {
        try {
            lobbyStore.saveLobby(lobby);
        } catch (SQLException e) {
            throw new LobbyManagementException("Failed to save lobby to the database");
        }
    }


    /**
     * Generates a unique lobby code.
     *
     * @return a unique lobby code
     */
    private String generateLobbyID() throws SQLException {
        String code;
        do {
            code = UUID.randomUUID()
                       .toString()
                       .substring(0, 8);
        } while (lobbyStore.getAllLobbies()
                           .containsKey(code));
        return code;
    }
}