package de.uol.swp.server.lobby.management;

import com.google.inject.Inject;
import de.uol.swp.common.lobby.dto.LobbyDTO;
import de.uol.swp.common.lobby.message.response.UserJoinedLobbyMessage;
import de.uol.swp.server.lobby.data.ILobby;
import de.uol.swp.server.lobby.data.Lobby;
import de.uol.swp.server.lobby.store.ILobbyStore;
import de.uol.swp.server.lobby.store.LobbyStore;
import de.uol.swp.server.lobby.store.LobbyStoreException;
import de.uol.swp.server.usermanagement.IUser;

import java.util.ArrayList;
import java.util.List;
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
    public LobbyManagement() {
        this.lobbyStore = LobbyStore.getInstance();
    }

    public ILobby createLobby(String name, IUser owner) {
        String lobbyID = generateLobbyID();
        List<IUser> users = new ArrayList<>();
        users.add(owner);
        return lobbyStore.createLobby(lobbyID, name, users, owner, 4);

    }

    public void leaveLobby(String lobbyID, IUser user) throws LobbyStoreException {
        ILobby lobbyToLeave = lobbyStore.findLobby(lobbyID);
        lobbyToLeave.getUsers()
                    .remove(user);
        lobbyStore.removeUser(lobbyID, user);
        if (user.getUsername()
                .equals(lobbyToLeave.getOwner()
                                    .getUsername()) && !lobbyToLeave.getUsers()
                                                                    .isEmpty()) {
            List<IUser> remainingUsers = lobbyToLeave.getUsers();
            if (!remainingUsers.isEmpty()) {
                IUser newOwner = remainingUsers.get(0);
                lobbyToLeave.updateOwner(newOwner);
            }
        }
        if (lobbyToLeave.getUsers()
                        .isEmpty()) {
            lobbyStore.removeLobby(lobbyID);
        }
    }

    public void deleteLobby(String lobbyId) throws LobbyStoreException {
        lobbyStore.removeLobby(lobbyId);
    }

    public ILobby getLobby(String lobbyID) {
        return lobbyStore.findLobby(lobbyID);
    }

    public List<ILobby> getLobbies() {
        return new ArrayList<>(lobbyStore.getAllLobbies()
                                         .values());
    }

    @Override
    public void joinLobby(ILobby lobby, IUser user) throws LobbyStoreException {
        String lobbyID = lobby.getLobbyCode();
        lobbyStore.joinUser(lobbyID, user);
    }

    public ILobby updateLobby(ILobby lobby) {
        return lobbyStore.updateLobby(lobby.getLobbyCode(),
                lobby.getName(),
                lobby.getUsers(),
                lobby.getOwner(),
                lobby.getDifficulty()
        );
    }


    /**
     * Generates a unique lobby code.
     *
     * @return a unique lobby code
     */
    private String generateLobbyID() {
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