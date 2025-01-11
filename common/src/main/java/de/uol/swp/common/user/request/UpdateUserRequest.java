package de.uol.swp.common.user.request;

import de.uol.swp.common.message.request.AbstractRequestMessage;
import de.uol.swp.common.user.IUserDTO;

import java.util.Objects;

/**
 * Request to update an user
 *
 * @see IUserDTO
 * @author Marco Grawunder
 * @since 2019-09-02
 */
public class UpdateUserRequest extends AbstractRequestMessage {

    private final IUserDTO toUpdate;

    /**
     * Constructor
     *
     * @param user the user object the sender shall be updated to unchanged fields
     *             being empty
     * @since 2019-09-02
     */
    public UpdateUserRequest(IUserDTO user){
        this.toUpdate = user;
    }

    /**
     * Getter for the updated user object
     *
     * @return the updated user object
     * @since 2019-09-02
     */
    public IUserDTO getUser() {
        return toUpdate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UpdateUserRequest that = (UpdateUserRequest) o;
        return Objects.equals(toUpdate, that.toUpdate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(toUpdate);
    }
}
