package de.uol.swp.server.lobby;

import de.uol.swp.common.lobby.ILobby;
import de.uol.swp.common.lobby.dao.LobbyDAO;
import de.uol.swp.common.lobby.dto.LobbyDTO;
import de.uol.swp.common.user.User;
import de.uol.swp.common.user.UserDTO;
import de.uol.swp.server.database.DatabaseConnection;
import de.uol.swp.server.player.Player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/**
 * Manages creation, deletion and storing of lobbies
 *
 * @author Marco Grawunder
 * @see ILobby
 * @see LobbyDTO
 * @since 2019-10-08
 */
public class LobbyManagement {

    private final Map<String, Lobby> lobbies = new HashMap<>();

    private static final String INSERT_LOBBY_SQL = "INSERT INTO Lobby (id, difficulty, owner, users) VALUES (?, ?, ?," + " ?)";
    private static final String INSERT_LOBBY_USER_SQL = "INSERT INTO User (userID, user_name, password) VALUES (?, ?," + " ?)";


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
    public void createLobby(String name, User owner) {

        if (lobbies.containsKey(name)) {
            throw new IllegalArgumentException("Lobby name " + name + " already exists!");
        }
        String lobbyCode = generateLobbyCode();
        List<User> users = new ArrayList<>();
        UserDTO ownerUser = new UserDTO(owner.getUsername(), owner.getPassword(), owner.getEMail());
        users.add(ownerUser);

        Lobby newLobby = new Lobby(name, lobbyCode, users, 4);
        lobbies.put(name, newLobby);

        try {
            LobbyDTO lobbyDTO = new LobbyDTO(name, owner, lobbyCode, 4);
            saveLobby(lobbyDTO);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to save lobby to the database");
        }
    }


    /**
     * Generates a unique lobby code.
     *
     * @return a unique lobby code
     */
    private String generateLobbyCode() {
        final String[] code = new String[1];
        do {
            code[0] = UUID.randomUUID()
                    .toString()
                    .substring(0, 8);
        } while (lobbies.values()
                .stream()
                .anyMatch(iLobby -> iLobby.getLobbyCode()
                        .equals(code[0])));
        return code[0];
    }


    /**
     * Deletes lobby with requested name
     *
     * @param name String containing the name of the lobby to delete
     * @throws IllegalArgumentException there exists no lobby with the  requested
     *                                  name
     * @since 2019-10-08
     */
    public void dropLobby(String name) {
        if (!lobbies.containsKey(name)) {
            throw new IllegalArgumentException("ILobby name " + name + " not found!");
        }
        lobbies.remove(name);
    }

    /**
     * Searches for the lobby with the requested name
     *
     * @param name String containing the name of the lobby to search for
     * @return either empty Optional or Optional containing the lobby
     * @see Optional
     * @since 2019-10-08
     */
    public Optional<ILobby> getLobby(String name) {
        Lobby lobby = lobbies.get(name);
        if (lobby != null) {
            return Optional.of(lobby);
        }
        return Optional.empty();
    }

    public void saveLobby(LobbyDTO lobby) throws SQLException {
        DatabaseConnection dbConnection = DatabaseConnection.getInstance();

        try (Connection connection = dbConnection.getConnection()) {
            connection.setAutoCommit(false);

            try {
                try (
                        PreparedStatement psLobby = connection.prepareStatement(
                                INSERT_LOBBY_SQL,
                                Statement.RETURN_GENERATED_KEYS
                        )
                ) {
                    psLobby.setString(1, lobby.getLobbyCode());
                    psLobby.setInt(2, lobby.getDifficulty());
                    psLobby.setObject(2, lobby.getOwner());
                    psLobby.setObject(3, lobby.getUsers());

                    psLobby.executeUpdate();

                }

                for (User user : lobby.getUsers()) {
                    try (
                            PreparedStatement psUser = connection.prepareStatement(
                                    INSERT_LOBBY_USER_SQL,
                                    Statement.RETURN_GENERATED_KEYS
                            )
                    ) {
                        psUser.setString(1, user.getEMail());
                        psUser.setString(2, user.getPassword());
                        psUser.executeUpdate();

                    }

                }

                connection.commit();

            } catch (SQLException e) {
                connection.rollback();
                throw e;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }

    }
}
