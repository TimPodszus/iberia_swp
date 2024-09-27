package de.uol.swp.server.lobby.store;

import de.uol.swp.common.lobby.dto.LobbyDTO;
import de.uol.swp.common.user.User;
import de.uol.swp.common.user.UserDTO;
import de.uol.swp.server.database.DatabaseConnection;
import de.uol.swp.server.lobby.Lobby;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LobbyStore implements ILobbyStore {
    private static final String INSERT_LOBBY_SQL = "INSERT INTO Lobby (lobbyID, difficulty, owner, lobbyname) VALUES (?, ?, ?, ?)";
    private static final String INSERT_LOBBYUSERS_SQL ="INSERT INTO LobbyUsers (lobbyID, username) VALUES (?, ?)";

    @Override
    public Lobby findLobby(String lobbyID) throws SQLException {
        String sql = "SELECT lobbyID, difficulty, owner, lobbyname FROM Lobby WHERE lobbyID = ?";

        DatabaseConnection dbConnection = DatabaseConnection.getInstance();
        try (Connection connection = dbConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, lobbyID);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String lobbyCode = rs.getString("lobbyID");
                    int difficulty = rs.getInt("difficulty");
                    String ownerUsername = rs.getString("owner");
                    String lobbyName = rs.getString("lobbyname");

                    User owner = findUserByUsername(ownerUsername);

                    List<User> users = findUsersByLobbyCode(lobbyCode);

                    return new Lobby(lobbyName, lobbyCode, users, owner, difficulty);
                } else {
                    return null;
                }
            }
        }
    }

    public User findUserByUsername(String username) throws SQLException {
        String sql = "SELECT username, password FROM User WHERE username = ?";

        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String password = rs.getString("password");
                    UserDTO userDTO = new UserDTO(username, password);
                    return userDTO.getWithoutPassword();
                }
            }
        }
        return null;
    }

    private List<User> findUsersByLobbyCode(String lobbyCode) throws SQLException {
        String sql = "SELECT username FROM LobbyUsers WHERE lobbyID = ?";
        List<User> users = new ArrayList<>();

        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, lobbyCode);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String username = rs.getString("username");
                    User user = findUserByUsername(username);
                    if (user != null) {
                        users.add(user);
                    }
                }
            }
        }
        return users;
    }

    @Override
    public Lobby createLobby(String lobbyName, String lobbycode, List<User> users, User owner, int difficulty) throws SQLException {
        Lobby newLobby = new Lobby(lobbyName, lobbycode, users, owner, difficulty);
        saveLobby(new LobbyDTO(lobbyName, owner, lobbycode, difficulty));
        return newLobby;
    }

    @Override
    public Lobby updateLobby(String lobbyName, String lobbycode, List<User> users, User owner, int difficulty) {
        // not implemented
        return null;
    }

    public void removeUser(String lobbyID, User user) throws SQLException, LobbyStoreException {
        String sql = "DELETE FROM LobbyUsers WHERE lobbyID = ? AND username = ?";

        DatabaseConnection dbConnection = DatabaseConnection.getInstance();
        try (Connection connection = dbConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, lobbyID);
            ps.setString(2, user.getUsername());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new LobbyStoreException("User to remove does not exist in this lobby!");
        }
    }



    @Override
    public void removeLobby(String lobbyID) throws SQLException, LobbyStoreException {
        String sql = "DELETE FROM Lobby WHERE lobbyID = ?";

        DatabaseConnection dbConnection = DatabaseConnection.getInstance();
        try (Connection connection = dbConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, lobbyID);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new LobbyStoreException("Lobby to remove does not exist!");
        }
    }

    @Override
    public Map<String, Lobby> getAllLobbies() throws SQLException {
        String sql = "SELECT lobbyID, difficulty, owner, lobbyname FROM Lobby";

        Map<String, Lobby> lobbies = new HashMap<>();

        DatabaseConnection dbConnection = DatabaseConnection.getInstance();
        try (Connection connection = dbConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                String lobbyCode = rs.getString("lobbyID");
                int difficulty = rs.getInt("difficulty");
                String ownerUsername = rs.getString("owner");
                String lobbyName = rs.getString("lobbyname");

                User owner = findUserByUsername(ownerUsername);

                List<User> users = findUsersByLobbyCode(lobbyCode);

                Lobby lobby = new Lobby(lobbyName, lobbyCode, users, owner, difficulty);

                lobbies.put(lobbyName, lobby);
            }
        }

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
