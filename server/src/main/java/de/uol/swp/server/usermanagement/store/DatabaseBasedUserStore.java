package de.uol.swp.server.usermanagement.store;


import de.uol.swp.server.database.DatabaseConnection;
import de.uol.swp.server.usermanagement.IUser;
import de.uol.swp.server.usermanagement.User;
import de.uol.swp.server.usermanagement.management.UserManagementException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;


public class DatabaseBasedUserStore extends AbstractUserStore implements UserStore {
    private static final Logger LOG = LogManager.getLogger(DatabaseBasedUserStore.class);
    private final Connection connection;

    public DatabaseBasedUserStore() {
        try {
            connection = DatabaseConnection.getInstance()
                                           .getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public Optional<IUser> findUser(String username, String password) {
        try (
                PreparedStatement ps = connection.prepareStatement(
                        "SELECT username, password FROM User WHERE username = ? and password = ?")
        ) {
            ps.setString(1, username);
            ps.setString(2, password);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    User user = new User(rs.getString("username"), rs.getString("password"));
                    return Optional.of(user);
                }
            }
        } catch (Exception e) {
            LOG.error(e);
        }
        return Optional.empty();
    }

    @Override
    public Optional<IUser> findUser(String username) {
        try (PreparedStatement ps = connection.prepareStatement("SELECT username, password FROM User WHERE username = ?")) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    User user = new User(rs.getString("username"), rs.getString("password"));
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
     * @return the created User object, or null if the user could not be created
     * @throws UserManagementException if the username or password is empty, or if a user with the specified username already exists
     */
    @Override
    public IUser createUser(String username, String password) throws UserManagementException {

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
        Optional<IUser> user = findUser(username);
        if (user.isPresent()) {
            return user.get();
        } else {
            throw new UserManagementException("Failed to retrieve the created user");
        }
    }


    @Override
    public IUser createUser(IUser user) {
        String username = user.getUsername();
        String password = user.getPassword();
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
        Optional<IUser> returnuser = findUser(username);
        if (returnuser.isPresent()) {
            return returnuser.get();
        } else {
            throw new UserManagementException("Failed to retrieve the created user");
        }
    }

    @Override
    public IUser updateUser(String username, String password) {
        if (username.isEmpty() || password.isEmpty()) {
            throw new UserManagementException("Password and username cannot be empty");
        }
        try (PreparedStatement ps = connection.prepareStatement("UPDATE User SET password = ? WHERE username = ?")) {
            ps.setString(1, password);
            ps.setString(2, username);
            ps.executeUpdate();
        } catch (SQLException e) {
            LOG.error(e);
        }
        Optional<IUser> user = findUser(username);
        if (user.isPresent()) {
            return user.get();
        } else {
            throw new UserManagementException("Failed to retrieve the updated user");
        }

    }


    @Override
    public void removeUser(String username) {
        // TODO
    }

    @Override
    public List<IUser> getAllUsers() {
        return List.of();
    }


}
