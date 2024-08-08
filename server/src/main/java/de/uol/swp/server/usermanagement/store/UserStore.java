package de.uol.swp.server.usermanagement.store;

import de.uol.swp.common.user.User;

import java.util.List;
import java.util.Optional;

/**
 * Interface to unify different kinds of UserStores in order to able to exchange
 * them easily.
 *
 * @author Marco Grawunder
 * @since 2019-08-13
 */
public interface UserStore {

    /**
     * Find a user by username and password
     *
     * @param username username of the user to find
     * @param password password of the user to find
     * @return The User without password information, if found
     * @since 2019-08-13
     */
    Optional<User> findUser(String username, String password);

    /**
     * Find a user only by name
     *
     * @param username username of the user to find
     * @return The User without password information, if found
     * @since 2019-08-13
     */
    Optional<User> findUser(String username);

    /**
     * Create a new user
     *
     * @param username username of the new user
     * @param password password the user wants to use
     * @param eMail email address of the new user
     * @return The User without password information
     * @since 2019-08-13
     */
    User createUser(String username, String password, String eMail);

    /**
     * Update user. Update only given fields. Username cannot be changed
     *
     * @param username username of the user to be modified
     * @param password new password
     * @param eMail new email address
     * @return The User without password information
     * @since 2019-08-13
     */
    User updateUser(String username, String password, String eMail);

    /**
     * Remove user from store
     *
     * @param username the username of the user to remove
     * @since 2019-10-10
     */
    void removeUser(String username);


    /**
     * Retrieves the list of all users.
     * @return A list of all users without password information
     * @since 2019-08-13
     */
    List<User> getAllUsers();


}
