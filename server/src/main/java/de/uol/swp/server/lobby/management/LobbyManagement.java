package de.uol.swp.server.lobby.management;

import com.google.inject.Inject;
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
    @Inject
    public LobbyManagement(ILobbyStore lobbyStore) {
        this.lobbyStore = lobbyStore;
    }

    public ILobby createLobby(String name, User owner) throws LobbyManagementException {
        try {
            String lobbyID = generateLobbyID();
            List<User> users = new ArrayList<>();
            users.add(owner);
            return lobbyStore.createLobby(lobbyID, name, users, owner, 4);
        } catch (SQLException e) {
            throw new LobbyManagementException("Failed to create lobby: " + e.getMessage());
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

    public List<ILobby> getLobbies() throws LobbyManagementException {
        try {
            return new ArrayList<>(lobbyStore.getAllLobbies()
                                             .values());
        } catch (SQLException e) {
            throw new LobbyManagementException("Failed to get lobbies");
        }
    }

    public void updateLobby(ILobby lobby) throws LobbyManagementException {
        try {
            lobbyStore.updateLobby(lobby.getName(),
                    lobby.getLobbyCode(),
                    lobby.getUsers(),
                    lobby.getOwner(),
                    lobby.getDifficulty()
            );
        } catch (SQLException e) {
            throw new LobbyManagementException("Failed to update lobby");
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