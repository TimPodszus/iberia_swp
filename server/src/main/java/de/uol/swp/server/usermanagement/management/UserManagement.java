package de.uol.swp.server.usermanagement.management;

import com.google.inject.Inject;
import de.uol.swp.server.usermanagement.IUser;
import de.uol.swp.server.usermanagement.store.UserStore;

import java.util.Optional;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * Handles most user related issues e.g. login/logout
 *
 * @author Marco Grawunder
 * @see AbstractUserManagement
 * @since 2019-08-05
 */
public class UserManagement extends AbstractUserManagement {

    private final UserStore userStore;
    private final SortedMap<String, IUser> loggedInUsers = new TreeMap<>();

    /**
     * Constructor
     *
     * @param userStore object of the UserStore to be used
     * @see de.uol.swp.server.usermanagement.store.UserStore
     * @since 2019-08-05
     */
    @Inject
    public UserManagement(UserStore userStore) {
        this.userStore = userStore;
    }

    /**
     * Logs in a user with the given username and password.
     *
     * @param username the username of the user
     * @param password the password of the user
     * @return the logged-in user
     * @throws SecurityException if the user cannot be authenticated
     */
    @Override
    public IUser login(String username, String password) {
        Optional<IUser> user = userStore.findUser(username, password);
        if (user.isPresent()) {
            this.loggedInUsers.put(username, user.get());
            return user.get();
        } else {
            throw new SecurityException("Cannot auth user " + username);
        }
    }

    /**
     * Checks if a user is logged in.
     *
     * @param username the user to check
     * @return true if the user is logged in, false otherwise
     */
    @Override
    public boolean isLoggedIn(IUser username) {
        return loggedInUsers.containsKey(username.getUsername());
    }

    /**
     * Creates a new user.
     *
     * @param userToCreate the user to create
     * @return the created user
     * @throws UserManagementException if the username is already used
     */
    @Override
    public IUser createUser(IUser userToCreate) {
        Optional<IUser> user = userStore.findUser(userToCreate.getUsername());
        if (user.isPresent()) {
            throw new UserManagementException("Username already used!");
        }
        return userStore.createUser(userToCreate);
    }


    /**
     * Logs out the specified user.
     *
     * @param user the user to log out
     */
    @Override
    public void logout(IUser user) {
        loggedInUsers.remove(user.getUsername());
    }

    /**
     * Retrieves a user by their username.
     *
     * @param username the username of the user to retrieve
     * @return the user associated with the specified username
     * @throws UserManagementException if the username is unknown
     */
    @Override
    public IUser getUser(String username) {
        return userStore.findUser(username)
                        .orElseThrow(() -> new UserManagementException("Username unknown!"));
    }

    /**
     * Changes the password of a user.
     *
     * @param username    the username of the user whose password is to be changed
     * @param newPassword the new password to set
     */
    public void changePassword(String username, String newPassword) {
        userStore.updateUser(username, newPassword);
    }
}
