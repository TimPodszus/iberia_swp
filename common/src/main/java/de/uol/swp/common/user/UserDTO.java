package de.uol.swp.common.user;

import java.util.Objects;

/**
 * Objects of this class are used to transfer user data between the server and the
 * clients.
 *
 * @author Marco Grawunder
 * @see de.uol.swp.common.user.User
 * @see de.uol.swp.common.user.request.RegisterUserRequest
 * @see de.uol.swp.common.user.response.AllOnlineUsersResponse
 * @since 2019-08-13
 */
public class UserDTO implements User {

    private final String username;
    private final String password;

    /**
     * Constructor
     *
     * @param username username of the user
     * @param password password the user uses
     * @since 2019-08-13
     */
    public UserDTO(String username, String password) {
        if (Objects.nonNull(username) && Objects.nonNull(password)) {
            this.username = username;
            this.password = password;
        }else{
            throw new IllegalArgumentException("Username and password cannot be null");
        }
    }

    /**
     * Copy constructor
     *
     * @param user User object to copy the values of
     * @return UserDTO copy of User object
     * @since 2019-08-13
     */
    public static UserDTO create(User user) {
        return new UserDTO(user.getUsername(), user.getPassword());
    }

    /**
     * Copy constructor leaving password variable empty
     * This constructor is used for the user list, because it would be a major security
     * flaw to send all user data including passwords to everyone connected.
     *
     * @param user User object to copy the values of
     * @return UserDTO copy of User object having the password variable left empty
     * @since 2019-08-13
     */
    public static UserDTO createWithoutPassword(User user) {
        return new UserDTO(user.getUsername(), "");
    }


    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public User getWithoutPassword() {
        return new UserDTO(username, "");
    }

    @Override
    public int compareTo(User o) {
        return username.compareTo(o.getUsername());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserDTO userDTO = (UserDTO) o;
        return Objects.equals(username, userDTO.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username);
    }
}
