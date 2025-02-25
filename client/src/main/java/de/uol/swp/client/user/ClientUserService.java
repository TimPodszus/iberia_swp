package de.uol.swp.client.user;


import de.uol.swp.common.user.IUserDTO;

/**
 * An interface for all methods of the client user service
 * As the communication with the server is based on events, the
 * returns of the call must be handled by events
 *
 * @author Marco Grawunder
 * @since 2017-03-17
 */

public interface ClientUserService {

    /**
     * Login with username and password
     *
     * @param username the name of the user
     * @param password the password of the user
     * @since 2017-03-17
     */
    void login(String username, String password);

    /**
     * Log out from server
     *
     * @implNote the User Object has to contain a unique identifier in order to
     * remove the correct user
     * @since 2017-03-17
     */
    void logout(IUserDTO user);

    /**
     * Create a new persistent user
     *
     * @param user The user to create
     * @implNote the User Object has to contain a unique identifier in order to
     * remove the correct user
     * @since 2019-09-02
     */
    void createUser(IUserDTO user);


    /**
     * Change the password of a user
     *
     * @param user        The user whose password is to be changed
     * @param newPassword The new password to set
     * @since 2019-09-02
     */
    void changePassword(IUserDTO user, String newPassword);
}
