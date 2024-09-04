package de.uol.swp.common.user.request;

import de.uol.swp.common.message.AbstractRequestMessage;
import de.uol.swp.common.passwordHashing.PasswordHashing;
import lombok.Getter;
import lombok.Setter;
import org.apache.directory.api.ldap.model.password.PasswordUtil;

import java.util.Objects;

/**
 * A request send from client to server, trying to log in with
 * username and password
 * 
 * @author Marco Grawunder
 * @since  2017-03-17
 */
@Getter
public class LoginRequest extends AbstractRequestMessage {

	private static final long serialVersionUID = 7793454958390539421L;

    @Setter
    private String username;
	@Setter
	private byte[] hashedPassword;

	/**
	 * Constructor
	 *
	 * @param username username the user tries to log in with
	 * @param hashedPassword hashed password the user tries to log in with
	 * @since  2017-03-17
	 */
	public LoginRequest(String username, byte[] hashedPassword) {
		this.username = username;
		this.hashedPassword = hashedPassword;
	}

	@Override
	public boolean authorizationNeeded() {
		return false;
	}

	public void setHashedPassword(String password) {
		hashedPassword= PasswordHashing.hashPassword(password);

	}



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LoginRequest that = (LoginRequest) o;
        return Objects.equals(username, that.username) &&
            PasswordUtil.compareCredentials(hashedPassword, that.hashedPassword);

    }

    @Override
    public int hashCode() {
        return Objects.hash(username, hashedPassword);
    }
}
