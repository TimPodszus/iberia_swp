package de.uol.swp.common.lobby.message;

import de.uol.swp.common.message.AbstractServerMessage;
import de.uol.swp.common.user.User;
import de.uol.swp.common.user.UserDTO;

import java.util.Objects;

/**
 * Base class of all lobby messages. Basic handling of lobby data.
 *
 * @see de.uol.swp.common.user.User
 * @see de.uol.swp.common.message.AbstractServerMessage
 * @author Marco Grawunder
 * @since 2019-10-08
 */
public class AbstractLobbyMessage extends AbstractServerMessage {

    String name;
    UserDTO user;

    /**
     * Default constructor
     *
     * @implNote this constructor is needed for serialization
     * @since 2019-10-08
     */
    public AbstractLobbyMessage() {
    }

    /**
     * Constructor
     *
     * @param name name of the lobby
     * @param user user responsible for the creation of this message
     * @since 2019-10-08
     */
    public AbstractLobbyMessage(String name, UserDTO user) {
        this.name = name;
        this.user = user;
    }

    /**
     * Getter for the name variable
     *
     * @return String containing the lobby's name
     * @since 2019-10-08
     */
    public String getName() {
        return name;
    }

    /**
     * Setter for the name variable
     *
     * @param name  String containing the lobby's name
     * @since 2019-10-08
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Getter for the user variable
     *
     * @return User responsible for the creation of this message
     * @since 2019-10-08
     */
    public User getUser() {
        return user;
    }
    /**
     * Setter for the user variable
     *
     * @param user  User responsible for the creation of this message
     * @since 2019-10-08
     */
    public void setUser(UserDTO user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AbstractLobbyMessage that = (AbstractLobbyMessage) o;
        return Objects.equals(name, that.name) &&
                Objects.equals(user, that.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, user);
    }
}
