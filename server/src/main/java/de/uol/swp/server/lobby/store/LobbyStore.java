package de.uol.swp.server.lobby.store;

import de.uol.swp.server.lobby.data.ILobby;
import de.uol.swp.server.lobby.data.Lobby;
import de.uol.swp.server.usermanagement.IUser;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LobbyStore implements ILobbyStore {
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
    public ILobby findLobby(String lobbycode) {
        return lobbies.get(lobbycode);
    }

    @Override
    public ILobby createLobby(String lobbyCode, String name, List<IUser> users, IUser owner, int difficulty) {
        ILobby lobby = new Lobby(lobbyCode, name, users, owner, difficulty);
        lobbies.put(lobbyCode, lobby);
        return lobby;
    }

    @Override
    public ILobby updateLobby(String name, String lobbycode, List<IUser> users, IUser owner, int difficulty) {
        ILobby lobby = new Lobby(lobbycode, name, users, owner, difficulty);
        lobbies.put(lobbycode, lobby);
        return lobby;
    }

    @Override
    public void removeLobby(String name) throws LobbyStoreException {
        ILobby lobby = lobbies.remove(name);
        if (lobby == null) {
            throw new LobbyStoreException(LOBBY_NOT_FOUND + name);
        }
    }

    @Override
    public Map<String, ILobby> getAllLobbies() {
        return lobbies;
    }

    @Override
    public void saveLobby(ILobby lobby) {
        lobbies.put(lobby.getLobbyCode(), lobby);
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
    public void joinUser(String lobbyCode, IUser user) throws LobbyStoreException {
        ILobby lobby = lobbies.get(lobbyCode);
        if (lobby == null) {
            throw new LobbyStoreException(LOBBY_NOT_FOUND + lobbyCode);
        }
        lobby.getUsers()
             .add(user);
    }
}
