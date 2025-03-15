package de.uol.swp.common.user.response;

import de.uol.swp.common.message.response.AbstractResponseMessage;
import de.uol.swp.common.user.IUserDTO;

import java.util.Objects;

/**
 * A message containing the session (typically for a new logged in user)
 * This response is sent to the Client whose LoginRequest was successful
 *
 * @see de.uol.swp.common.user.request.LoginRequest
 * @see IUserDTO
 * @author Marco Grawunder
 * @since 2019-08-07
 */
public class LoginSuccessfulResponse extends AbstractResponseMessage {

    private static final long serialVersionUID = -9107206137706636541L;

    private final IUserDTO user;

    /**
     * Constructor
     *
     * @param user the user who successfully logged in
     * @since 2019-08-07
     */
    public LoginSuccessfulResponse(IUserDTO user) {
        this.user = user;
    }

    /**
     * Getter for the User variable
     *
     * @return User object of the user who successfully logged in
     * @since 2019-08-07
     */
    public IUserDTO getUser() {
        return user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LoginSuccessfulResponse that = (LoginSuccessfulResponse) o;
        return Objects.equals(user, that.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user);
    }
}
