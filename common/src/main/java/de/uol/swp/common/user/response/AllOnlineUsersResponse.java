package de.uol.swp.common.user.response;

import de.uol.swp.common.message.response.AbstractResponseMessage;
import de.uol.swp.common.user.IUserDTO;
import de.uol.swp.common.user.UserDTO;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * Response message for the RetrieveAllOnlineUsersRequest
 * This message gets sent to the client that sent an RetrieveAllOnlineUsersRequest.
 * It contains a List with User objects of every user currently logged in to the
 * server.
 *
 * @author Marco Grawunder
 * @see AbstractResponseMessage
 * @see de.uol.swp.common.user.request.RetrieveAllOnlineUsersRequest
 * @see IUserDTO
 * @since 2019-08-13
 */
public class AllOnlineUsersResponse extends AbstractResponseMessage {

    private final ArrayList<IUserDTO> users = new ArrayList<>();

    /**
     * Default Constructor
     *
     * @implNote this constructor is needed for serialization
     * @since 2019-08-13
     */
    public AllOnlineUsersResponse() {
        // needed for serialization
    }

    /**
     * Constructor
     * This constructor generates a new List of the logged in users from the given
     * Collection. The significant difference between the two being that the new
     * List contains copies of the User objects. These copies have their password
     * variable set to an empty String.
     *
     * @param users Collection of all users currently logged in
     * @since 2019-08-13
     */
    public AllOnlineUsersResponse(Collection<IUserDTO> users) {
        for (IUserDTO user : users) {
            this.users.add(new UserDTO(user.getUsername(), user.getPassword()));
        }
    }

    /**
     * Getter for the list of users currently logged in
     *
     * @return list of users currently logged in
     * @since 2019-08-13
     */
    public List<IUserDTO> getUsers() {
        return users;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        AllOnlineUsersResponse that = (AllOnlineUsersResponse) o;
        return Objects.equals(users, that.users);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), users);
    }
}
