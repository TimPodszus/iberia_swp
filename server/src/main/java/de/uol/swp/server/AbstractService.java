package de.uol.swp.server;

import com.google.inject.Inject;
import de.uol.swp.common.cards.data.ICardDTO;
import de.uol.swp.common.message.AbstractServerMessage;
import de.uol.swp.common.message.Message;
import de.uol.swp.common.message.ServerMessage;
import de.uol.swp.common.user.Session;
import de.uol.swp.server.cards.CardMapper;
import de.uol.swp.server.cards.data.ICard;
import de.uol.swp.server.city.data.ICity;
import de.uol.swp.server.lobby.data.ILobby;
import de.uol.swp.server.usermanagement.AuthenticationService;
import org.greenrobot.eventbus.EventBus;

import java.util.*;

/**
 * This class is the base for creating a new Service.
 * This class prepares the child classes to have the EventBus set and methods post
 * and sendToAll implemented in order to reduce unnecessary code repetition.
 *
 * @author Marco Grawunder
 * @since 2019-10-08
 */
public class AbstractService {

    protected static final String USER_NOT_LOGGED_IN = "User not logged in";

    protected static final int DEFAULT_MESSAGE_DELAY_MILLIS = 200;
    /**
     * The EventBus instance used for posting and handling events.
     * This is a protected final field, ensuring it is initialized once and cannot be changed.
     */
    protected final EventBus bus;

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
    public void post(Message message) {
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
     * Sends a message to all users in a specified lobby.
     * <p>
     * This method prepares an AbstractServerMessage to be sent to all users in the given lobby
     * and posts it to the EventBus.
     *
     * @param lobby   the lobby whose users will receive the message
     * @param message the message to be sent to all users in the lobby
     * @see AbstractServerMessage
     * @since 2019-10-08
     */
    public void sendToAllInLobby(ILobby lobby, AbstractServerMessage message) {
        List<Session> sessions = authenticationService.getSessions(new HashSet<>(lobby.getUsers()));
        message.setReceiver(sessions);
        post(message);
    }

    /**
     * Converts a map of available destinations from ICity and ICard to a map of Integer and ICardDTO.
     *
     * @param availableDestinations the map of available destinations with ICity as keys and lists of ICard as values
     * @return a map of available destinations with Integer as keys and lists of ICardDTO as values
     */
    protected Map<Integer, List<ICardDTO>> convertToDtoMap(Map<ICity, List<ICard>> availableDestinations) {
        Map<Integer, List<ICardDTO>> availableDestinationsAsDtos = new HashMap<>();

        for (Map.Entry<ICity, List<ICard>> entry : availableDestinations.entrySet()) {
            List<ICardDTO> cards = entry.getValue()
                                        .stream()
                                        .map(CardMapper::toDTO)
                                        .toList();
            availableDestinationsAsDtos.put(entry.getKey()
                                                 .getId(), cards);
        }

        return availableDestinationsAsDtos;
    }
}
