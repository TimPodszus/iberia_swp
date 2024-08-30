package de.uol.swp.server.usermanagement.store;

import de.uol.swp.common.user.User;
import de.uol.swp.common.user.UserDTO;
import de.uol.swp.server.database.DatabaseConnection;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;

public class DatabaseBasedUserStore extends AbstractUserStore implements UserStore{


    public Optional<User> findUser(String username, String password) {
        try {
            // Get the connection to the database
            DatabaseConnection dbConnection = DatabaseConnection.getInstance();
            Connection connection = dbConnection.getConnection();
            // Prepare the SQL statement
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM User WHERE username = ? and password = ?");
            ps.setString(1, username);
            ps.setString(2, password);
            //Execute the query
            ResultSet rs = ps.executeQuery();
            // Check if the user exists
            while (rs.next()) {
                //return UserDTO object
                UserDTO user = new UserDTO(rs.getString("username"), rs.getString("password"));
                return Optional.of(user);
            }

        } catch (Exception e) {

            // Display the DB exception if any
            System.out.println(e);
        }
        return Optional.empty();
    }

    @Override
    public Optional<User> findUser(String username)
    {
        return Optional.empty();
    }

    @Override
    public User createUser(String username, String password)
    {
        return null;
    }

    @Override
    public User updateUser(String username, String password)
    {
        return null;
    }


    @Override
    public void removeUser(String username)
    {

    }

    @Override
    public List<User> getAllUsers()
    {
        return List.of();
    }


}
