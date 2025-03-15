package de.uol.swp.common.user;

import java.io.Serializable;

/**
 * Interface representing a User Data Transfer Object (DTO).
 * This interface extends Serializable and Comparable interfaces.
 */
public interface IUserDTO extends Serializable, Comparable<IUserDTO> {

    /**
     * Gets the username of the user.
     *
     * @return the username of the user
     */
    String getUsername();

    /**
     * Gets the password of the user.
     *
     * @return the password of the user
     */
    String getPassword();

    /**
     * Returns a new IUserDTO instance without the password.
     *
     * @return a new IUserDTO instance without the password
     */
    IUserDTO getWithoutPassword();
}