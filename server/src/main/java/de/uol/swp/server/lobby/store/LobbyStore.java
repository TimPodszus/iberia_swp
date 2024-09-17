package de.uol.swp.server.lobby.store;

import de.uol.swp.common.lobby.ILobby;
import de.uol.swp.common.lobby.dto.LobbyDTO;
import de.uol.swp.common.user.User;
import de.uol.swp.server.lobby.Lobby;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class LobbyStore implements ILobbyStore {
    private final Map<String, Lobby> lobbies = new HashMap<>();

    @Override
    public Optional<ILobby> findLobby(String lobbycode) {
        ILobby lobby = lobbies.get(lobbycode);
        if (lobby != null && Objects.equals(lobby.getLobbyCode(), lobbycode)) {
            return Optional.of(lobby);
        }
        return Optional.empty();
    }

    @Override
    public Lobby createLobby(String name, String lobbycode, List<User> users, int difficulty) {
        return new Lobby(name, lobbycode, users, difficulty);
    }

    @Override
    public Lobby updateLobby(String name, String lobbycode, List<User> users, int difficulty) {
        return createLobby(name, lobbycode, users, difficulty);
    }



    @Override
    public void removeLobby(String name) {
        lobbies.remove(name);
    }

    @Override
    public List<Lobby> getAllLobbies() {
        return new ArrayList<>(lobbies.values());
    }


    @Override
    public void saveLobby(LobbyDTO lobbyDTO) throws SQLException {

    }

}
