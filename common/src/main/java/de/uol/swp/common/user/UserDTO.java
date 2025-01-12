package de.uol.swp.common.user;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;


/**
 * Objects of this class are used to transfer user data between the server and the
 * clients.
 *
 * @author Marco Grawunder
 * @see IUserDTO
 * @see de.uol.swp.common.user.request.RegisterUserRequest
 * @see de.uol.swp.common.user.response.AllOnlineUsersResponse
 * @since 2019-08-13
 */

@Getter
@AllArgsConstructor
public class UserDTO implements IUserDTO {
    private String username;

    private String password;

    public static IUserDTO createWithoutPassword(IUserDTO defaultUser) {
        return new UserDTO(defaultUser.getUsername(), "");
    }
    public UserDTO(IUserDTO user) {
        this.username = user.getUsername();
        this.password = user.getPassword();
    }

    @Override
    public IUserDTO getWithoutPassword() {
        return new UserDTO(username, "");
    }

    @Override
    public int compareTo(IUserDTO o) {
        return username.compareTo(o.getUsername());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        UserDTO userDTO = (UserDTO) obj;
        return Objects.equals(username, userDTO.username) && Objects.equals(password, userDTO.password);
    }

    @Override
    public int hashCode() {
        return username.hashCode() + password.hashCode();
    }
}
