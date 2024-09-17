package de.uol.swp.server.lobby.store;

import de.uol.swp.common.lobby.ILobby;
import de.uol.swp.common.lobby.dto.LobbyDTO;
import de.uol.swp.common.user.User;
import de.uol.swp.server.lobby.Lobby;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface ILobbyStore {
    Optional<ILobby> findLobby(String lobbycode);

    Lobby createLobby(String name, String lobbycode, List<User> users, int difficulty);

    Lobby updateLobby(String name, String password, List<User> users, int difficulty);

    void removeLobby(String name);

    List<Lobby> getAllLobbies();

    void saveLobby(LobbyDTO lobbyDTO) throws SQLException;
}
