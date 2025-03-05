package de.uol.swp.server.cards;

import com.google.inject.Inject;
import de.uol.swp.common.cards.request.PlayCardRequest;
import de.uol.swp.common.game.dto.IGameDTO;
import de.uol.swp.common.game.message.event.BoardUpdateEvent;
import de.uol.swp.common.game.message.response.StatusResponse;
import de.uol.swp.common.user.IUserDTO;
import de.uol.swp.common.user.Session;
import de.uol.swp.server.AbstractService;
import de.uol.swp.server.cards.events.SecondChanceEvent;
import de.uol.swp.server.cards.management.CardNotFoundException;
import de.uol.swp.server.cards.management.CardNotPlayableException;
import de.uol.swp.server.cards.management.ICardManagement;
import de.uol.swp.server.game.GameMapper;
import de.uol.swp.server.game.exceptions.GameException;
import de.uol.swp.server.lobby.data.ILobby;
import de.uol.swp.server.lobby.management.ILobbyManagement;
import de.uol.swp.server.usermanagement.IUser;
import de.uol.swp.server.usermanagement.management.ServerUserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class CardService extends AbstractService {
    private static final Logger LOG = LogManager.getLogger(CardService.class);

    /**
     * For testing purposes these are private and not final
     */
    private ICardManagement cardManagement;

    /**
     * For testing purposes these are private and not final
     */
    private ILobbyManagement lobbyManagement;

    /**
     * For testing purposes these are private and not final
     */
    private ServerUserService userManagement;

    /**
     * Constructor
     *
     * @param bus the EvenBus used throughout the server
     */
    @Inject
    public CardService(
            EventBus bus,
            ICardManagement cardManagement,
            ILobbyManagement lobbyManagement,
            ServerUserService userManagement
    ) {
        super(bus);
        this.cardManagement = cardManagement;
        this.lobbyManagement = lobbyManagement;
        this.userManagement = userManagement;
    }

    @Subscribe
    public void onPlayCardRequest(PlayCardRequest request) throws GameException {
        LOG.debug("[LobbyId: {}] Received PlayCardRequest", request.getLobbyId());
        IUserDTO user = request.getSession()
                               .orElseThrow(() -> {
                                   LOG.error("[LobbyId: {}] Session missing in PlayCardRequest", request.getLobbyId());
                                   return new GameException("Session missing in PlayCardRequest");
                               })
                               .getUser();

        try {
            cardManagement.playCard(request.getLobbyId(), user.getUsername(), request.getCardId());
        } catch (CardNotPlayableException e) {
            sendStatusResponse(request, false, "Die Eventkarte kann nicht gespielt werden");

            return;
        }

        LOG.info("[LobbyId: {}] Played the card. Sending BoardUpdateEvent", request.getLobbyId());
        IGameDTO game = GameMapper.toDTO(cardManagement.getGame(request.getLobbyId()));
        BoardUpdateEvent event = new BoardUpdateEvent(request.getLobbyId(), game);
        ILobby lobby = lobbyManagement.getLobby(request.getLobbyId());
        sendToAllInLobby(lobby, event);
        sendServerMessageEvent(request.getLobbyId(), user.getUsername() + " hat eine Karte gespielt.");
    }

    /**
     * Handles the SecondChanceEvent by playing a second chance card for the specified user.
     * If the card is not found, it returns the last played card and sends a status response.
     * Finally, it sends a BoardUpdateEvent to all users in the lobby.
     *
     * @param event the SecondChanceEvent containing the lobby ID and username
     * @throws GameException if the user is not logged in or if there is an error during card play
     */
    @Subscribe
    public void onSecondChanceEvent(SecondChanceEvent event) throws GameException {
        LOG.debug("[LobbyId: {}] Received SecondChanceEvent", event.getLobbyId());
        IUser user = userManagement.getUser(event.getUsername());
        Session session = authenticationService.getSession(user)
                                               .orElseThrow(() -> {
                                                   LOG.error(USER_NOT_LOGGED_IN);
                                                   return new GameException(USER_NOT_LOGGED_IN);
                                               });

        try {
            cardManagement.playSecondChanceCard(event.getLobbyId(), user.getUsername());
        } catch (CardNotFoundException e) {
            cardManagement.returnLastPlayedCard(event.getLobbyId(), user.getUsername());

            StatusResponse response = new StatusResponse(event.getLobbyId(),
                    false,
                    "CityCard not found in discard pile"
            );
            response.setSession(session);
            post(response);
        }

        LOG.info("[LobbyId: {}] Played SecondChanceEventCard. Sending BoardUpdateEvent", event.getLobbyId());
        IGameDTO game = GameMapper.toDTO(cardManagement.getGame(event.getLobbyId()));
        BoardUpdateEvent boardUpdateEvent = new BoardUpdateEvent(event.getLobbyId(), game);
        ILobby lobby = lobbyManagement.getLobby(event.getLobbyId());
        sendToAllInLobby(lobby, boardUpdateEvent);
        sendServerMessageEvent(event.getLobbyId(), user.getUsername() + "hat die Eventkarte zweite Chance ausgespielt");
    }
}
