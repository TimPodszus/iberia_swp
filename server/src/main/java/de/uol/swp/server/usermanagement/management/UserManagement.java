package de.uol.swp.server.usermanagement.management;

import com.google.common.base.Strings;
import com.google.inject.Inject;
import de.uol.swp.server.usermanagement.IUser;
import de.uol.swp.server.usermanagement.store.UserStore;

import java.util.List;
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

    @Override
    public boolean isLoggedIn(IUser username) {
        return loggedInUsers.containsKey(username.getUsername());
    }

    @Override
    public IUser createUser(IUser userToCreate) {
        Optional<IUser> user = userStore.findUser(userToCreate.getUsername());
        if (user.isPresent()) {
            throw new UserManagementException("Username already used!");
        }
        return userStore.createUser(userToCreate);
    }

    @Override
    public IUser updateUser(IUser userToUpdate) {
        Optional<IUser> user = userStore.findUser(userToUpdate.getUsername());
        if (user.isEmpty()) {
            throw new UserManagementException("Username unknown!");
        }
        // Only update if there are new values
        String newPassword = firstNotNull(
                userToUpdate.getPassword(),
                user.get()
                    .getPassword()
        );
        return userStore.updateUser(userToUpdate.getUsername(), newPassword);

    }

    @Override
    public void dropUser(IUser userToDrop) {
        Optional<IUser> user = userStore.findUser(userToDrop.getUsername());
        if (user.isEmpty()) {
            throw new UserManagementException("Username unknown!");
        }
        userStore.removeUser(userToDrop.getUsername());

    }

    /**
     * Sub-function of update user
     * This method is used to set the new user values to the old ones if the values
     * in the update request were empty.
     *
     * @param firstValue  value to update to, empty String or null
     * @param secondValue the old value
     * @return byte[] containing the value to be used in the update command
     * @since 2019-08-05
     */
    private String firstNotNull(String firstValue, String secondValue) {
        return Strings.isNullOrEmpty(firstValue) ? secondValue : firstValue;
    }

    @Override
    public void logout(IUser user) {
        loggedInUsers.remove(user.getUsername());
    }

    @Override
    public List<IUser> retrieveAllUsers() {
        return userStore.getAllUsers();
    }

    @Override
    public IUser getUser(String username) {
        return userStore.findUser(username)
                        .orElseThrow(() -> new UserManagementException("Username unknown!"));
    }

    public void changePassword(String username, String newPassword) {
        userStore.updateUser(username, newPassword);
    }
}
