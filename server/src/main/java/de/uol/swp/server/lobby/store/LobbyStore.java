package de.uol.swp.server.lobby.store;

import de.uol.swp.common.lobby.ILobby;
import de.uol.swp.common.lobby.dto.LobbyDTO;
import de.uol.swp.common.user.User;
import de.uol.swp.server.database.DatabaseConnection;
import de.uol.swp.server.lobby.Lobby;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class LobbyStore implements ILobbyStore {
    private final Map<String, Lobby> lobbies = new HashMap<>();
    private static final String INSERT_LOBBY_SQL = "INSERT INTO Lobby (lobbyID, difficulty, owner, lobbyname) VALUES (?, ?, ?, ?)";
    private static final String INSERT_LOBBYUSERS_SQL ="INSERT INTO LobbyUsers (lobbyID, username) VALUES (?, ?)";

    @Override
    public Optional<ILobby> findLobby(String lobbycode) {
        ILobby lobby = lobbies.get(lobbycode);
        if (lobby != null && Objects.equals(lobby.getLobbyCode(), lobbycode)) {
            return Optional.of(lobby);
        }
        return Optional.empty();
    }

    @Override
    public Lobby createLobby(String name, String lobbycode, List<User> users, User owner, int difficulty) throws SQLException {
        Lobby newLobby = new Lobby(name, lobbycode, users, owner, difficulty);
        lobbies.put(name, newLobby);
        saveLobby(new LobbyDTO(name, owner, lobbycode, difficulty));
        return newLobby;
    }

    @Override
    public Lobby updateLobby(String name, String lobbycode, List<User> users, User owner, int difficulty) throws SQLException {
        Lobby newLobby = new Lobby(name, lobbycode, users, owner, difficulty);
        lobbies.put(name, newLobby);
        saveLobby(new LobbyDTO(name, owner, lobbycode, difficulty));
        return createLobby(name, lobbycode, users, owner, difficulty);
    }

    @Override
    public void removeLobby(String name) {
        lobbies.remove(name);
        // Datenbankaufruf um eine bestehende Lobby zu löschen fehlt noch
    }

    @Override
    public Map<String, Lobby> getAllLobbies() {
        return lobbies;
    }

    @Override
    public void saveLobby(LobbyDTO lobbyDTO) throws SQLException {
        DatabaseConnection dbConnection = DatabaseConnection.getInstance();
        try (Connection connection = dbConnection.getConnection()) {
            connection.setAutoCommit(false);
            try {


                try (PreparedStatement psLobby = connection.prepareStatement(
                        INSERT_LOBBY_SQL, Statement.RETURN_GENERATED_KEYS)) {
                    psLobby.setString(1, lobbyDTO.getLobbyCode());
                    psLobby.setInt(2, lobbyDTO.getDifficulty());
                    psLobby.setString(3, lobbyDTO.getOwner().getUsername());
                    psLobby.setString(4, lobbyDTO.getName());
                    psLobby.executeUpdate();
                }

                for (User user : lobbyDTO.getUsers()) {
                    try (PreparedStatement psLobbyUsers = connection.prepareStatement(
                            INSERT_LOBBYUSERS_SQL, Statement.RETURN_GENERATED_KEYS)) {
                        psLobbyUsers.setString(1, lobbyDTO.getLobbyCode());
                        psLobbyUsers.setString(2, user.getUsername());
                        psLobbyUsers.executeUpdate();
                    }
                }

                connection.commit();
            } catch (SQLException e) {
                connection.rollback();
                throw e;
            }
        }
    }

}
