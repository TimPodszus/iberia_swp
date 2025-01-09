package de.uol.swp.client.user;

import de.uol.swp.common.user.IUserDTO;
import lombok.Getter;
import lombok.Setter;

/**
 * A singleton class that stores the current user.
 */
@Getter
@Setter
public class UserStore {
    private static UserStore instance;

    /**
     * The current user.
     */
    private IUserDTO user;

    /**
     * Private constructor to prevent instantiation.
     */
    private UserStore() {
    }

    /**
     * Returns the singleton instance of UserStore.
     * If the instance is null, it creates a new one.
     *
     * @return the singleton instance of UserStore
     */
    public static UserStore getInstance() {
        if (instance == null) {
            instance = createInstance();
        }
        return instance;
    }

    /**
     * Creates a new instance of UserStore in a thread-safe manner.
     *
     * @return the singleton instance of UserStore
     */
    private static synchronized UserStore createInstance() {
        if (instance == null) {
            instance = new UserStore();
        }
        return instance;
    }
}
