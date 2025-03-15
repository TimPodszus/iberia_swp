package de.uol.swp.common.lobby.message.request;

import de.uol.swp.common.message.request.AbstractRequestMessage;
import de.uol.swp.common.user.IUserDTO;
import lombok.NoArgsConstructor;


/**
 * Request sent to the server when a user wants to create a new lobby
 *
 * @author Marco Grawunder
 * @see de.uol.swp.common.lobby.message.AbstractLobbyRequest
 * @see IUserDTO
 * @since 2019-10-08
 */
@NoArgsConstructor
public class CreateLobbyRequest extends AbstractRequestMessage {

}
