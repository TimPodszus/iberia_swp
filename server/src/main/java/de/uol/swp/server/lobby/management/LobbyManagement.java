package de.uol.swp.server.lobby.management;

import com.google.inject.Inject;
import de.uol.swp.common.lobby.dto.LobbyDTO;
import de.uol.swp.server.lobby.data.ILobby;
import de.uol.swp.server.lobby.data.Lobby;
import de.uol.swp.server.lobby.exceptions.LobbyIsFullException;
import de.uol.swp.server.lobby.exceptions.LobbyNotFoundException;
import de.uol.swp.server.lobby.exceptions.UserAlreadyInLobbyException;
import de.uol.swp.server.lobby.store.ILobbyStore;
import de.uol.swp.server.lobby.store.LobbyStore;
import de.uol.swp.server.usermanagement.IUser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
    private static final Logger LOG = LogManager.getLogger(LobbyManagement.class);
    private static final int MAX_PLAYERS_IN_LOBBY = 5;
    private final ILobbyStore lobbyStore;

    /**
     * Constructs a new LobbyManagement instance and initializes the lobby store.
     */
    @Inject
    public LobbyManagement() {
        this.lobbyStore = LobbyStore.getInstance();
    }

    @Override
    public ILobby createLobby(IUser owner) {
        LOG.debug("Creating lobby for user {}", owner.getUsername());
        String lobbyID = generateLobbyID();
        String lobbyName = "Lobby " + owner.getUsername();
        List<IUser> users = new ArrayList<>();
        users.add(owner);
        ILobby lobby = lobbyStore.createLobby(lobbyID, lobbyName, users, owner, 4);
        LOG.info("[LobbyId: {}]: Lobby created", lobby.getLobbyId());
        return lobby;
    }

    @Override
    public void leaveLobby(String lobbyID, IUser user) {
        LOG.debug("[LobbyId: {}] User {} is leaving lobby", lobbyID, user.getUsername());
        ILobby lobby = lobbyStore.findLobby(lobbyID);
        
        lobby.removeUser(user);
        if (user.getUsername()
                .equals(lobby.getOwner()
                             .getUsername()) && !lobby.getUsers()
                                                      .isEmpty()) {
            List<IUser> remainingUsers = lobby.getUsers();
            if (!remainingUsers.isEmpty()) {
                IUser newOwner = remainingUsers.get(0);
                LOG.debug("[LobbyId: {}] Owner left lobby, assigning {} as new owner", lobbyID, newOwner.getUsername());
                lobby.updateOwner(newOwner);
            }
        }
        if (lobby.getUsers()
                 .isEmpty()) {
            LOG.info("[LobbyId: {}] Lobby is empty, deleting lobby", lobbyID);
            lobbyStore.removeLobby(lobbyID);
        }
    }

    @Override
    public void deleteLobby(String lobbyId) {
        lobbyStore.removeLobby(lobbyId);
        LOG.info("[LobbyId: {}] Lobby deleted", lobbyId);
    }

    @Override
    public ILobby getLobby(String lobbyID) {
        LOG.info("Retrieving lobby with ID {}", lobbyID);
        return lobbyStore.findLobby(lobbyID);
    }

    @Override
    public List<ILobby> getLobbies() {
        LOG.info("Retrieving all lobbies");
        return new ArrayList<>(lobbyStore.getAllLobbies()
                                         .values());
    }

    @Override
    public void joinLobby(String lobbyId, IUser user) throws LobbyNotFoundException, LobbyIsFullException, UserAlreadyInLobbyException {
        LOG.debug("[LobbyId: {}] User {} is joining lobby", lobbyId, user.getUsername());
        ILobby lobby = lobbyStore.findLobby(lobbyId);
        if (lobby == null) {
            LOG.error("[LobbyId: {}] Lobby not found", lobbyId);
            throw new LobbyNotFoundException("No lobby found to join");
        }
        if (lobby.getUsers().size() >= MAX_PLAYERS_IN_LOBBY) {
            LOG.error("[LobbyId: {}] Lobby is full", lobbyId);
            throw new LobbyIsFullException("Lobby is full");
        }
        if (lobby.getUsers().contains(user)) {
            LOG.warn("[LobbyId: {}] User {} is already in lobby", lobbyId, user.getUsername());
            throw new UserAlreadyInLobbyException("User is already in lobby");
        }
        lobby.addUser(user);
        LOG.info("[LobbyId: {}] A User joined lobby", lobbyId);
    }

    @Override
    public ILobby updateLobby(ILobby lobby) {
        LOG.debug("Updating lobby with ID {}", lobby.getLobbyId());
        return lobbyStore.saveLobby(lobby);
    }

    @Override
    public ILobby removeUser(String lobbyId, String username) throws LobbyNotFoundException {
        LOG.debug("[LobbyId: {}] Removing user {}", lobbyId, username);
        ILobby lobby = lobbyStore.findLobby(lobbyId);
        if (lobby == null) {
            LOG.error("[LobbyId: {}] Lobby not found", lobbyId);
            throw new LobbyNotFoundException("Lobby not found");
        }
        IUser user = lobby.getUser(username);
        if (user != null) {
            lobby.removeUser(user);
            LOG.info("[LobbyId: {}] User has been removed from lobby", lobbyId);
        } else {
            LOG.warn("[LobbyId: {}] User {} not found in lobby", lobbyId, username);
        }
        return lobby;
    }


    /**
     * Generates a unique lobby code.
     *
     * @return a unique lobby code
     */
    private String generateLobbyID() {
        LOG.debug("Generating lobby code");
        String code;
        do {
            code = UUID.randomUUID()
                       .toString()
                       .substring(0, 8);
        } while (lobbyStore.getAllLobbies()
                           .containsKey(code));
        LOG.debug("Generated lobby code: {}", code);
        return code;
    }
}