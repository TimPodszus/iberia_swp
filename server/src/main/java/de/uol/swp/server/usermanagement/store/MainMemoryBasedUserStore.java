package de.uol.swp.server.usermanagement.store;

import com.google.common.base.Strings;
import de.uol.swp.common.user.User;
import de.uol.swp.common.user.UserDTO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * This is a user store.
 * This is the user store that is used for the start of the software project. The
 * user accounts in this user store only reside within the RAM of your computer
 * and only for as long as the server is running. Therefore the users have to be
 * added every time the server is started.
 *
 * @implNote This store will never return the password of a user!
 * @see de.uol.swp.server.usermanagement.store.AbstractUserStore
 * @see de.uol.swp.server.usermanagement.store.UserStore
 * @author Marco Grawunder
 * @since 2019-08-05
 */

/**
 * This is a user store.
 * This is the user store that is used for the start of the software project. The
 * user accounts in this user store only reside within the RAM of your computer
 * and only for as long as the server is running. Therefore the users have to be
 * added every time the server is started.
 *
 * @implNote This store will never return the password of a user!
 * @see de.uol.swp.server.usermanagement.store.AbstractUserStore
 * @see de.uol.swp.server.usermanagement.store.UserStore
 * @author Marco Grawunder
 * @since 2019-08-05
 */

public class MainMemoryBasedUserStore extends AbstractUserStore implements UserStore {

    private final Map<String, User> users = new HashMap<>();

    @Override
    public Optional<User> findUser(String username, String password) {
        User usr = users.get(username);
        if (usr != null && Objects.equals(usr.getPassword(),password)) {
            return Optional.of(usr.getWithoutPassword());
        }
        return Optional.empty();
    }

    @Override
    public Optional<User> findUser(String username) {
        User usr = users.get(username);
        if (usr != null) {
            return Optional.of(usr.getWithoutPassword());
        }
        return Optional.empty();
    }

    @Override
    public User createUser(String username, String password) {
        if (Strings.isNullOrEmpty(username)){
            throw new IllegalArgumentException("Username must not be null");
        }
        User usr = new UserDTO(username, password);
        users.put(username, usr);
        return usr;

    }


    public User createUser(User userToCreate) {
        if (Strings.isNullOrEmpty(userToCreate.getUsername())){
            throw new IllegalArgumentException("Username must not be null");
        }

        users.put(userToCreate.getUsername(), userToCreate);
        return userToCreate;
    }


    @Override
    public User updateUser(String username, String password) {
        return createUser(username, password);
    }


    @Override
    public void removeUser(String username) {
        users.remove(username);
    }

    @Override
    public List<User> getAllUsers() {
        List<User> retUsers = new ArrayList<>();
        users.values().forEach(u -> retUsers.add(u.getWithoutPassword()));
        return retUsers;
    }
    public User getUser(int id) {
        List<User> retUsers = new ArrayList<>();
        users.values().forEach(u -> retUsers.add(u.getWithoutPassword()));
        return retUsers.get(id);
    }
}
