package de.uol.swp.server.usermanagement.store;

import de.uol.swp.common.user.User;
import de.uol.swp.common.user.UserDTO;
import de.uol.swp.server.database.DatabaseConnection;
import de.uol.swp.server.usermanagement.UserManagementException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;


public class DatabaseBasedUserStore extends AbstractUserStore implements UserStore
{
    private static final Logger LOG = LogManager.getLogger(DatabaseBasedUserStore.class);
    private final Connection connection;

    public DatabaseBasedUserStore()
    {
        try {
            connection = DatabaseConnection.getInstance()
                                           .getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Optional<User> findUser(String username, String password)
    {
        try (
                PreparedStatement ps = connection.prepareStatement(
                        "SELECT username, password FROM User WHERE username = ? and password = ?")
        ) {
            ps.setString(1, username);
            ps.setString(2, password);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    UserDTO user = new UserDTO(rs.getString("username"), rs.getString("password"));
                    return Optional.of(user);
                }
            }
        } catch (Exception e) {
            LOG.error(e);
        }
        return Optional.empty();
    }

    @Override
    public Optional<User> findUser(String username)
    {
        try (PreparedStatement ps = connection.prepareStatement("SELECT username, password FROM User WHERE username = ?")) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    UserDTO user = new UserDTO(rs.getString("username"), rs.getString("password"));
                    return Optional.of(user);
                }
            }
        } catch (Exception e) {
            LOG.error(e);
        }
        return Optional.empty();
    }

    /**
     * Creates a new user with the specified username and password.
     *
     * @param username the username of the new user
     * @param password the password of the new user
     *
     * @return the created User object, or null if the user could not be created
     *
     * @throws UserManagementException if the username or password is empty, or if a user with the specified username already exists
     */
    @Override
    public User createUser(String username, String password) throws UserManagementException
    {

        if (username.isEmpty() || password.isEmpty()) {
            throw new UserManagementException("Password and username cannot be empty");
        }

        try (PreparedStatement ps = connection.prepareStatement("INSERT INTO User (username, password) VALUES (?, ?)")) {
            ps.setString(1, username);
            ps.setString(2, password);
            ps.executeUpdate();
        } catch (SQLException e) {
            LOG.error(e);
        }
        Optional<User> user = findUser(username);
        if (user.isPresent()) {
            return user.get();
        } else {
            throw new UserManagementException("Failed to retrieve the created user");
        }
    }

    /**
     * Creates a new user with the specified username and password.
     *
     * This method converts the password from a byte array to a UTF-8 encoded string
     * and then calls the overloaded createUser method that accepts a string password.
     *
     * @param username the username of the new user
     * @param password the password of the new user as a byte array
     *
     * @return the created User object
     */
    @Override
    public User createUser(String username, byte[] password)
    {
        return createUser(username, new String(password, StandardCharsets.UTF_8));
    }

    @Override
    public User updateUser(String username, String password)
    {
        return null;
    }

    @Override
    public User updateUser(String username, byte[] password)
    {
        return null;
    }


    @Override
    public void removeUser(String username)
    {
        // TODO
    }

    @Override
    public List<User> getAllUsers()
    {
        return List.of();
    }


}
