package de.uol.swp.server.communication;

import de.uol.swp.common.user.IUserDTO;
import de.uol.swp.common.user.Session;
import de.uol.swp.common.user.request.LoginRequest;
import de.uol.swp.server.usermanagement.IUser;
import de.uol.swp.server.usermanagement.UserMapper;

import java.util.Objects;
import java.util.UUID;

/**
 * Class used to store connected clients and Users in an identifiable way
 *
 * @see de.uol.swp.server.usermanagement.AuthenticationService#onLoginRequest(LoginRequest)
 * @see de.uol.swp.common.user.Session
 * @author Marco Grawunder
 * @since 2017-03-17
 */
public class UUIDSession implements Session {

	private final String sessionId;
    private final IUser user;

	/**
	 * private Constructor
	 *
	 * @param user the user connected to the session
	 * @since 2017-03-17
	 */
	private UUIDSession(IUser user) {
		synchronized (UUIDSession.class) {
			this.sessionId = String.valueOf(UUID.randomUUID());
            this.user = user;
		}
	}

	/**
	 * Builder for the UUIDSession
	 * Builder exposed to every class in the server, used since the constructor is private
	 *
	 * @param user the user connected to the session
	 * @return a new UUIDSession object for the user
	 * @since 2019-08-07
	 */
	public static Session create(IUser user) {
		return new UUIDSession(user);
	}

    @Override
	public String getSessionId() {
		return sessionId;
	}

	@Override
	public IUserDTO getUser() {return UserMapper.toDTO(user);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		UUIDSession session = (UUIDSession) o;
		return Objects.equals(sessionId, session.sessionId);
	}

	@Override
	public int hashCode() {
		return Objects.hash(sessionId);
	}

	@Override
	public String toString() {
		return "SessionId: "+sessionId;
	}
	
}
