package de.uol.swp.server.usermanagement.store;

import de.uol.swp.common.user.User;
import de.uol.swp.common.user.UserDTO;
import de.uol.swp.server.database.DatabaseConnection;
import org.apache.logging.log4j.LogManager;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;


public class DatabaseBasedUserStore extends AbstractUserStore implements UserStore{
    private static final org.apache.logging.log4j.Logger LOG = LogManager.getLogger(DatabaseBasedUserStore.class);

    public Optional<User> findUser(String username, String password) {
        try {
            // Get the connection to the database
            DatabaseConnection dbConnection = DatabaseConnection.getInstance();
            Connection connection = dbConnection.getConnection();
            // Prepare the SQL statement
            PreparedStatement ps = connection.prepareStatement("SELECT username, password FROM User WHERE username " +
                    "= ? and password = ?");
            ps.setString(1, username);
            ps.setString(2, password);
            //Execute the query
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                //return UserDTO object
                UserDTO user = new UserDTO(rs.getString("username"), rs.getString("password"));
                return Optional.of(user);
            }

        } catch (Exception e) {
            LOG.error(e);
        }
        return Optional.empty();
    }

    @Override
    public Optional<User> findUser(String username)
    {
        try {
            // Get the connection to the database
            DatabaseConnection dbConnection = DatabaseConnection.getInstance();
            Connection connection = dbConnection.getConnection();

            PreparedStatement ps = connection.prepareStatement("SELECT username, password FROM 'User' WHERE username = ?");
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                //return UserDTO object
                UserDTO user = new UserDTO(rs.getString("username"), rs.getString("password"));
                return Optional.of(user);
            }
        }
        catch (Exception e) {
            System.out.println(e);
        }
        return Optional.empty();
    }

    @Override
    public User createUser(String username, String password)
    {
        return null;
    }

    @Override
    public User createUser(String username, byte[] password)
    {
        return null;
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
