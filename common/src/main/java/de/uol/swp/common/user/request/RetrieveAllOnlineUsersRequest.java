package de.uol.swp.common.user.request;

import de.uol.swp.common.message.AbstractRequestMessage;

/**
 * Request for initialising the user list in the client
 *
 * This message is sent during the initialization of the user list. The server will
 * respond with a AllOnlineUsersResponse.
 *
 * @see de.uol.swp.common.user.response.AllOnlineUsersResponse
 * @author Marco Grawunder
 * @since 2019-08-07
 */
public class RetrieveAllOnlineUsersRequest extends AbstractRequestMessage {


}
