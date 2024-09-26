package de.uol.swp.server.lobby;

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
 * @see de.uol.swp.server.lobby.Lobby
 * @see de.uol.swp.common.lobby.dto.LobbyDTO
 * @author Marco Grawunder
 * @see Lobby
 * @see LobbyDTO
 * @since 2019-10-08
 */
public class LobbyManagement {
    private LobbyStore lobbyStore = new LobbyStore();


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
        if (lobbyStore.findLobby(name) != null) {
            throw new LobbyManagementException("Lobby name " + name + " already exists!");
        }
        String lobbyID = generateLobbyID();
        List<User> users = new ArrayList<>();
        users.add(owner);
        try {
            LobbyDTO lobbyDTO = new LobbyDTO(name, owner, lobbyID, 4);
            lobbyStore.saveLobby(lobbyDTO);
        } catch (SQLException e) {
            throw new LobbyManagementException("Failed to save lobby to the database");
        }
        return new Lobby(name, lobbyID, users, owner, 4);
    }

    public void leaveLobby(String lobbyID, User user) throws SQLException {
        Lobby lobbyToLeave = lobbyStore.findLobby(lobbyID);
        lobbyToLeave.getUsers().remove(user);
        if (user.equals(lobbyToLeave.getOwner()) && !lobbyToLeave.getUsers().isEmpty()) {
            User newOwner = lobbyToLeave.getAllUsers().getFirst();
            lobbyToLeave.setOwner(newOwner);


        }
        if (lobbyToLeave.getUsers().isEmpty()) {
            lobbyStore.removeLobby(lobbyID);
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
        } while (lobbyStore.getAllLobbies().containsKey(code));
        return code;
    }

    /**
     * Deletes lobby with requested lobbycode
     *
     * @param lobbyID String containing the ID of the lobby to delete
     * @throws LobbyManagementException there exists no lobby with the requested
     *                                  ID
     * @since 2019-10-08
     */

    public void dropLobby(String lobbyID) throws SQLException, LobbyManagementException {
        if (lobbyStore.findLobby(lobbyID) == null) {
            throw new LobbyManagementException("LobbyID " + lobbyID + " not found!");
        }
        lobbyStore.removeLobby(lobbyID);
    }

    /**
     * Searches for the lobby with the requested lobbycode
     *
     * @param lobbyID String containing the ID of the lobby to search for
     * @return either empty Optional or Optional containing the lobby
     * @see Optional
     * @since 2019-10-08
     */
    public Optional<Lobby> getLobby(String lobbyID) throws SQLException {
        Lobby lobby = lobbyStore.findLobby(lobbyID);
        if (lobby != null) {
            return Optional.of(lobby);
        }
        return Optional.empty();
    }

    public void setLobbyStore(LobbyStore lobbyStore) {
        this.lobbyStore = lobbyStore;
    }
}