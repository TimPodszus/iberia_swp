package de.uol.swp.server.usermanagement;

import de.uol.swp.common.passwordHashing.PasswordHashing;

import lombok.Getter;

import java.util.Objects;

/**
 * Represents a user in the system.
 */
@Getter
public class User implements IUser, Comparable<User>
{

    private String username;
    private String password;

    /**
     * Constructs a new User with the specified username and password.
     * The password is hashed before being stored.
     *
     * @param username the username of the user
     * @param password the password of the user
     *
     * @throws IllegalArgumentException if the username or password is null
     */
    public User(String username, String password)
    {
        if (Objects.nonNull(username) && Objects.nonNull(password)) {
            this.username = username;
            this.password = PasswordHashing.hashPassword(password);
        } else {
            throw new IllegalArgumentException("Username and password cannot be null");
        }
    }

    /**
     * Constructs a new User with the specified username and an empty password.
     *
     * @param username the username of the user
     */
    public User(String username)
    {
        createWithoutPassword(new User(username, ""));
    }

    /**
     * Creates a new User instance without a password.
     *
     * @param user the user from which to create a new instance
     *
     * @return a new User instance with the same username and an empty password
     */
    public static User createWithoutPassword(IUser user)
    {
        return new User(user.getUsername(), "");
    }

    /**
     * Returns a new User instance without the password.
     *
     * @return a new User instance with the same username and an empty password
     */
    @Override
    public IUser getWithoutPassword()
    {
        return new User(username, "");
    }

    /**
     * Checks if this user is equal to another object.
     * Two users are considered equal if they have the same username.
     *
     * @param o the object to compare with
     *
     * @return true if the users are equal, false otherwise
     */
    @Override
    public boolean equals(Object o)
    {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        User user = (User) o;
        return Objects.equals(username, user.username);
    }

    /**
     * Returns the hash code of this user.
     * The hash code is based on the username.
     *
     * @return the hash code of this user
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(username);
    }


    @Override
    public int compareTo(User other)
    {
        return this.username.compareTo(other.username);
    }

}