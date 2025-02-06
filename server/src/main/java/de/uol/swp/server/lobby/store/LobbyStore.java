package de.uol.swp.server.lobby.store;

import de.uol.swp.server.lobby.data.ILobby;
import de.uol.swp.server.lobby.data.Lobby;
import de.uol.swp.server.usermanagement.IUser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LobbyStore implements ILobbyStore {
    private static final Logger LOG = LogManager.getLogger(LobbyStore.class);
    private static final String LOBBY_NOT_FOUND = "Lobby not found: ";
    private static LobbyStore instance = new LobbyStore();

    private final Map<String, ILobby> lobbies = new HashMap<>();

    /**
     * Returns the singleton instance of the LobbyStore.
     * If the instance is null, it creates a new one.
     *
     * @return the singleton instance of LobbyStore
     */
    public static LobbyStore getInstance() {
        if (instance == null) {
            instance = createInstance();
        }
        return instance;
    }

    /**
     * Creates a new instance of LobbyStore in a thread-safe manner.
     *
     * @return a new instance of LobbyStore
     */
    private static synchronized LobbyStore createInstance() {
        if (instance == null) {
            instance = new LobbyStore();
        }
        return instance;
    }

    @Override
    public ILobby findLobby(String lobbyId) {
        LOG.debug("[LobbyId: {}]: Searching for lobby", lobbyId);
        ILobby lobby = lobbies.get(lobbyId);

        if (lobby == null) {
            LOG.warn("[LobbyId: {}]: Lobby not found", lobbyId);
        } else {
            LOG.info("[LobbyId: {}]: Lobby found", lobbyId);
        }

        return lobby;
    }

    @Override
    public ILobby createLobby(String lobbyId, String name, List<IUser> users, IUser owner, int difficulty) {
        LOG.debug("[LobbyId: {}]: Creating lobby for {}", lobbyId, owner.getUsername());
        ILobby lobby = new Lobby(lobbyId, name, users, owner, difficulty);
        lobbies.put(lobbyId, lobby);
        LOG.info("[LobbyId: {}]: Lobby created", lobbyId);
        return lobbies.get(lobbyId);
    }

    @Override
    public void removeLobby(String lobbyId) {
        LOG.debug("[LobbyId: {}]: Removing lobby", lobbyId);
        ILobby lobby = lobbies.remove(lobbyId);
        if (lobby == null) {
            LOG.warn("[LobbyId: {}]: Could not remove lobby. Lobby was not found", lobbyId);
        } else {
            LOG.info("[LobbyId: {}]: Lobby removed", lobbyId);
        }
    }

    @Override
    public Map<String, ILobby> getAllLobbies() {
        LOG.info("Retrieving all lobbies");
        return lobbies;
    }

    @Override
    public ILobby saveLobby(ILobby lobby) {
        lobbies.put(lobby.getLobbyId(), lobby);
        LOG.info("[LobbyId: {}]: Lobby saved", lobby.getLobbyId());
        return lobbies.get(lobby.getLobbyId());
    }

    @Override
    public void removeUser(String lobbyID, IUser user) throws LobbyStoreException {
        ILobby lobby = lobbies.get(lobbyID);
        if (lobby == null) {
            throw new LobbyStoreException(LOBBY_NOT_FOUND + lobbyID);
        }
        lobby.getUsers()
             .remove(user);
    }

    @Override
    public void joinUser(String lobbyId, IUser user) throws LobbyStoreException {
        ILobby lobby = lobbies.get(lobbyId);
        if (lobby == null) {
            throw new LobbyStoreException(LOBBY_NOT_FOUND + lobbyId);
        }
        lobby.getUsers()
             .add(user);
    }
}
