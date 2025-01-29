package de.uol.swp.server.lobby.store;

import com.google.inject.Inject;


import de.uol.swp.server.database.DatabaseConnection;
import de.uol.swp.server.lobby.data.ILobby;
import de.uol.swp.server.lobby.data.Lobby;
import de.uol.swp.server.usermanagement.IUser;
import de.uol.swp.server.usermanagement.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The LobbyStore class provides methods to interact with the lobby data in the database.
 * It implements the ILobbyStore interface.
 */
public class DatabaseBasedLobbyStore {
    private static final String INSERT_LOBBY_SQL = "INSERT INTO Lobby (lobbyID, difficulty, owner, lobbyname) VALUES (?, ?, ?, ?)";
    private static final String INSERT_LOBBYUSERS_SQL = "INSERT INTO LobbyUsers (lobbyID, username) VALUES (?, ?)";

    private Connection connection;
    private static final Logger LOG = LogManager.getLogger(DatabaseBasedLobbyStore.class);

    /**
     * Constructs a new LobbyStore and initializes the database connection.
     *
     * @throws RuntimeException if a database access error occurs
     */
    @Inject
    public DatabaseBasedLobbyStore() {
        try {
            this.connection = DatabaseConnection.getInstance()
                                                .getConnection();
        } catch (SQLException e) {
            LOG.error(e.getMessage());
        }
    }

    public ILobby findLobby(String lobbyID) throws SQLException {


        if (connection.isClosed()) {
            this.connection = DatabaseConnection.getInstance()
                                                .getConnection();

        }


        String sql = "SELECT lobbyID, difficulty, owner, lobbyname FROM Lobby WHERE lobbyID = ?";

        try (
                PreparedStatement ps = connection.prepareStatement(sql)
        ) {

            ps.setString(1, lobbyID);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return createLobbyFromResultSet(rs);
                } else {
                    return null;
                }
            }

        }
    }

    /**
     * Finds a user by their username.
     *
     * @param username the username of the user to find
     * @return the User object without the password, or null if the user is not found
     * @throws SQLException if a database access error occurs
     */
    private IUser findUserByUsername(String username) throws SQLException {
        String sql = "SELECT username, password FROM User WHERE username = ?";

        try (
                PreparedStatement ps = this.connection.prepareStatement(sql)
        ) {

            ps.setString(1, username);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String password = rs.getString("password");
                    IUser user = new User(username, password);
                    return user.getWithoutPassword();
                }
            }
        }
        return null;
    }

    private List<IUser> findUsersByLobbyCode(String lobbyCode) throws SQLException {
        String sql = "SELECT username FROM LobbyUsers WHERE lobbyID = ?";
        List<IUser> users = new ArrayList<>();

        try (
                PreparedStatement ps = this.connection.prepareStatement(sql)
        ) {

            ps.setString(1, lobbyCode);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String username = rs.getString("username");
                    IUser user = findUserByUsername(username);
                    if (user != null) {
                        users.add(user);
                    }
                }
            }
        }
        return users;
    }

    public ILobby createLobby(
            String lobbyCode, String lobbyName, List<IUser> users, IUser owner, int difficulty
    ) throws SQLException {
        ILobby newLobby = new Lobby(lobbyCode, lobbyName, users, owner, difficulty);
        saveLobby(newLobby);
        return newLobby;
    }

    public ILobby updateLobby(
            String lobbycode, String lobbyName, List<IUser> users, IUser owner, int difficulty
    ) throws SQLException {
        String sql = "UPDATE Lobby SET lobbyname = ?, difficulty = ?, owner = ? WHERE lobbyID = ?";

        try (
                PreparedStatement ps = this.connection.prepareStatement(sql)
        ) {
            ps.setString(1, lobbyName);
            ps.setInt(2, difficulty);
            ps.setString(3, owner.getUsername());
            ps.setString(4, lobbycode);
            ps.executeUpdate();
        }
        return findLobby(lobbycode);
    }

    public void removeUser(String lobbyID, IUser user) throws LobbyStoreException {
        String sql = "DELETE FROM LobbyUsers WHERE lobbyID = ? AND username = ?";


        try (
                PreparedStatement ps = this.connection.prepareStatement(sql)
        ) {

            ps.setString(1, lobbyID);
            ps.setString(2, user.getUsername());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new LobbyStoreException("User to remove does not exist in this lobby!");
        }
    }

    public void removeLobby(String lobbyID) throws LobbyStoreException {
        String sql = "DELETE FROM Lobby WHERE lobbyID = ?";

        try (
                PreparedStatement ps = this.connection.prepareStatement(sql)
        ) {

            ps.setString(1, lobbyID);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new LobbyStoreException("Lobby to remove does not exist!");
        }
    }

    public void joinUser(String lobbyCode, IUser user) throws SQLException {


        try (PreparedStatement ps = connection.prepareStatement(INSERT_LOBBYUSERS_SQL)) {

            ps.setString(1, lobbyCode);
            ps.setString(2, user.getUsername());
            ps.executeUpdate();
        }
    }

    public Map<String, ILobby> getAllLobbies() throws SQLException {
        String sql = "SELECT lobbyID, difficulty, owner, lobbyname FROM Lobby";

        Map<String, ILobby> lobbies = new HashMap<>();

        try (
                PreparedStatement ps = this.connection.prepareStatement(sql); ResultSet rs = ps.executeQuery()
        ) {
            while (rs.next()) {
                Lobby lobby = createLobbyFromResultSet(rs);
                lobbies.put(lobby.getLobbyCode(), lobby);
            }
        }

        return lobbies;
    }

    public void saveLobby(ILobby lobby) throws SQLException {
        connection.setAutoCommit(false);
        try {
            saveLobbyToDatabase(lobby);
            saveUsersInLobbyToDatabase(lobby);
            this.connection.commit();
        } catch (SQLException e) {
            this.connection.rollback();
            throw e;
        }

    }

    /**
     * Saves the given lobby to the database.
     *
     * @param lobby the lobby to be saved
     * @throws SQLException if a database access error occurs
     */
    private void saveLobbyToDatabase(ILobby lobby) throws SQLException {
        try (
                PreparedStatement psLobby = this.connection.prepareStatement(INSERT_LOBBY_SQL,
                        Statement.RETURN_GENERATED_KEYS
                )
        ) {
            psLobby.setString(1, lobby.getLobbyCode());
            psLobby.setInt(2, lobby.getDifficulty());
            psLobby.setString(3,
                    lobby.getOwner()
                         .getUsername()
            );
            psLobby.setString(4, lobby.getName());
            psLobby.executeUpdate();
        }
    }

    /**
     * Saves the users in the given lobby to the database.
     *
     * @param lobby the lobby containing the users to be saved
     * @throws SQLException if a database access error occurs
     */
    private void saveUsersInLobbyToDatabase(ILobby lobby) throws SQLException {
        for (IUser user : lobby.getUsers()) {
            try (PreparedStatement psLobbyUsers = this.connection.prepareStatement(INSERT_LOBBYUSERS_SQL)) {
                psLobbyUsers.setString(1, lobby.getLobbyCode());
                psLobbyUsers.setString(2, user.getUsername());
                psLobbyUsers.executeUpdate();
            }
        }
    }

    /**
     * Creates a Lobby object from the given ResultSet.
     *
     * @param rs the ResultSet containing lobby data
     * @return a Lobby object created from the ResultSet data
     * @throws SQLException if a database access error occurs
     */
    private Lobby createLobbyFromResultSet(ResultSet rs) throws SQLException {
        String lobbyCode = rs.getString("lobbyID");
        int difficulty = rs.getInt("difficulty");
        String ownerUsername = rs.getString("owner");
        String lobbyName = rs.getString("lobbyname");

        IUser owner = findUserByUsername(ownerUsername);

        List<IUser> users = findUsersByLobbyCode(lobbyCode);

        return new Lobby(lobbyCode, lobbyName, users, owner, difficulty);
    }

}
