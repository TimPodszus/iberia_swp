package de.uol.swp.common.user;

import de.uol.swp.common.passwordHashing.PasswordHashing;
import lombok.Getter;

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
@Getter
public class UserDTO implements User
{
    private String username;
    private String password;


    /**
     * Constructor
     *
     * @param username username of the user
     * @param password password the user uses
     *
     * @since 2019-08-13
     */
    public UserDTO(String username, String password)
    {
        if (Objects.nonNull(username) && Objects.nonNull(password)) {
            this.username = username;
            this.password = PasswordHashing.hashPassword(password);
        }else{
            throw new IllegalArgumentException("Username and password cannot be null");
        }
    }

    public UserDTO(String username)
    {
        createWithoutPassword(new UserDTO(username, ""));
    }


    /**
     * Copy constructor leaving password variable empty
     * This constructor is used for the user list, because it would be a major security
     * flaw to send all user data including passwords to everyone connected.
     *
     * @param user User object to copy the values of
     *
     * @return UserDTO copy of User object having the password variable left empty
     *
     * @since 2019-08-13
     */
    public static UserDTO createWithoutPassword(User user) {
        return new UserDTO(user.getUsername(), "");
    }


    @Override
    public String getUsername() {
        return username;
    }

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
    public boolean equals(Object o)
    {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UserDTO userDTO = (UserDTO) o;
        return Objects.equals(username, userDTO.username);
    }


    @Override
    public int hashCode()
    {
        return Objects.hash(username);
    }
}
