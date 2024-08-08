package de.uol.swp.server.usermanagement.store;


import com.google.common.hash.Hashing;

import java.nio.charset.StandardCharsets;

/**
 * Base class for all kinds of different UserStores
 *
 * @author Marco Grawunder
 * @see de.uol.swp.server.usermanagement.store.UserStore
 * @since 2019-09-04
 */
public abstract class AbstractUserStore implements UserStore {

    /**
     * Calculates the hash for a given String
     *
     * @implSpec the hash method used is sha256
     * @param toHash the String to calculate the hash for
     * @return String containing the calculated hash
     * @since 2019-09-04
     */
    protected String hash(String toHash){
        return Hashing.sha256()
                .hashString(toHash, StandardCharsets.UTF_8)
                .toString();
    }

}
