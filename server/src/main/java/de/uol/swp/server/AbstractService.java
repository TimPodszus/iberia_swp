package de.uol.swp.server;

import com.google.inject.Inject;
import de.uol.swp.common.message.AbstractServerMessage;
import de.uol.swp.common.message.Message;
import de.uol.swp.common.message.ServerMessage;
import de.uol.swp.server.lobby.data.ILobby;
import de.uol.swp.server.lobby.management.LobbyManagement;
import de.uol.swp.server.lobby.management.LobbyManagementException;
import de.uol.swp.server.usermanagement.AuthenticationService;
import org.greenrobot.eventbus.EventBus;

import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;

/**
 * This class is the base for creating a new Service.
 * This class prepares the child classes to have the EventBus set and methods post
 * and sendToAll implemented in order to reduce unnecessary code repetition.
 *
 * @author Marco Grawunder
 * @since 2019-10-08
 */
public class AbstractService {

    /**
     * The EventBus instance used for posting and handling events.
     * This is a protected final field, ensuring it is initialized once and cannot be changed.
     */
    protected final EventBus bus;

    /**
     * The LobbyManagement instance used for managing lobbies.
     * This field is injected by the dependency injection framework.
     */
    @Inject
    protected LobbyManagement lobbyManagement;

    /**
     * The AuthenticationService instance used for handling user authentication.
     * This field is injected by the dependency injection framework.
     */
    @Inject
    protected AuthenticationService authenticationService;

    /**
     * Constructor
     *
     * @param bus the EvenBus used throughout the server
     * @since 2019-10-08
     */
    public AbstractService(EventBus bus) {
        this.bus = bus;
        bus.register(this);
    }

    /**
     * Posts a message on the EventBus
     *
     * @param message the message to post
     * @see de.uol.swp.common.message.Message
     * @since 2019-10-08
     */
    protected void post(Message message) {
        bus.post(message);
    }

    /**
     * Prepares a ServerMessage to be sent to all connected users and posts it to the
     * EventBus.
     *
     * @param message the message to be sent to every user
     * @see ServerMessage
     * @since 2019-10-08
     */
    public void sendToAll(ServerMessage message) {
        message.setReceiver(Collections.emptyList());
        post(message);
    }

    /**
     * Prepares a given ServerMessage to be sent to all players in the lobby and
     * posts it on the EventBus
     *
     * @param lobbyCode Code of the lobby the players are in
     * @param message   the message to the users
     * @see ServerMessage
     * @since 2019-10-08
     */
    public void sendToAllInLobby(String lobbyCode, AbstractServerMessage message) throws LobbyManagementException {
        Optional<ILobby> lobby = lobbyManagement.getLobby(lobbyCode);

        if (lobby.isPresent()) {
            message.setReceiver(authenticationService.getSessions(new HashSet<>((lobby.get()
                                                                                      .getUsers()))));
            post(message);
        }
    }
}
