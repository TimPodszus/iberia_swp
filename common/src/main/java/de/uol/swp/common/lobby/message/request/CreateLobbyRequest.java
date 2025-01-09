package de.uol.swp.common.lobby.message.request;

import de.uol.swp.common.lobby.message.AbstractLobbyRequest;
import de.uol.swp.common.user.IUserDTO;


/**
 * Request sent to the server when a user wants to create a new lobby
 *
 * @author Marco Grawunder
 * @see de.uol.swp.common.lobby.message.AbstractLobbyRequest
 * @see IUserDTO
 * @since 2019-10-08
 */
public class CreateLobbyRequest extends AbstractLobbyRequest {

    /**
     * Default constructor
     *
     * @implNote this constructor is needed for serialization
     * @since 2019-10-08
     */
    public CreateLobbyRequest() {
    }

    /**
     * Constructor
     *
     * @param name  name of the lobby
     * @param owner User trying to create the lobby
     * @since 2019-10-08
     */
    public CreateLobbyRequest(String name, IUserDTO owner) {
        super(name, owner);
    }

    /**
     * Setter for the user variable
     *
     * @param owner User trying to create the lobby
     * @since 2019-10-08
     */
    public void setOwner(IUserDTO owner) {
        setUser(owner);
    }

    /**
     * Getter for the user variable
     *
     * @return User trying to create the lobby
     * @since 2019-10-08
     */
    public IUserDTO getOwner() {
        return getUser();
    }

}
