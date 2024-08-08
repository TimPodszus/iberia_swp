package de.uol.swp.common.user.request;

import de.uol.swp.common.message.AbstractRequestMessage;

/**
 * A request send from client to server to log out
 *
 * This Message should be used when implementing the Logout feature
 *
 * @author Marco Grawunder
 * @since 2019-08-07
 */

public class LogoutRequest extends AbstractRequestMessage{
	
	private static final long serialVersionUID = -5912075449879112061L;

	/**
	 * Constructor
	 *
	 * @since 2019-08-07
	 */
	public LogoutRequest() {
		super();
	}

}
