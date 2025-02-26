package de.uol.swp.server.player;

import com.google.inject.Inject;
import de.uol.swp.common.cards.data.ICardDTO;
import de.uol.swp.common.game.message.event.BoardUpdateEvent;
import de.uol.swp.common.game.message.request.ShareRideRequest;
import de.uol.swp.common.game.message.response.StatusResponse;
import de.uol.swp.common.message.response.AbstractResponseMessage;
import de.uol.swp.common.player.request.*;
import de.uol.swp.common.user.Session;
import de.uol.swp.server.AbstractService;
import de.uol.swp.server.cards.CardMapper;
import de.uol.swp.server.cards.data.ICard;
import de.uol.swp.server.game.GameMapper;
import de.uol.swp.server.game.data.IGame;
import de.uol.swp.server.game.management.IGameManagement;
import de.uol.swp.server.player.data.CardsAmountChangeListener;
import de.uol.swp.server.player.data.IPlayer;
import de.uol.swp.server.player.management.IPlayerManagement;
import de.uol.swp.server.player.management.PlayerManagementException;
import de.uol.swp.server.usermanagement.IUser;
import de.uol.swp.server.usermanagement.UserMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

/**
 * Service class for handling player-related operations.
 */
public class PlayerService extends AbstractService implements CardsAmountChangeListener {
    public static final Logger LOG = LogManager.getLogger(PlayerService.class);
    private final IPlayerManagement playerManagement;
    private final IGameManagement gameManagement;

    private static final String SESSION_NOT_PRESENT = "Session not present";

    /**
     * Constructs a new PlayerService.
     *
     * @param bus              the EventBus instance for event handling
     * @param playerManagement the player management instance for player operations
     */
    @Inject
    public PlayerService(EventBus bus, IPlayerManagement playerManagement, IGameManagement gameManagement) {
        super(bus);
        this.playerManagement = playerManagement;
        this.gameManagement = gameManagement;

        playerManagement.setCardsAmountChangeListener(this);
    }

    /**
     * Handles the DrawPlayerCardRequest event.
     *
     * @param request the request to draw a player card
     */
    @Subscribe
    public void onDrawPlayerCardRequest(DrawPlayerCardRequest request) {
        AbstractResponseMessage response;
        Session session = request.getSession()
                                 .orElseThrow(() -> new IllegalStateException(SESSION_NOT_PRESENT));
        try {
            ICardDTO card = playerManagement.drawPlayerCard(request.getLobbyId(), UserMapper.toUser(session.getUser()));
            response = new DrawPlayerCardResponse(request.getLobbyId(), true, "Card drawn successfully", card);
        } catch (PlayerManagementException e) {
            response = new StatusResponse(request.getLobbyId(), false, "Error drawing a player card");
        }
        response.setSession(session);
        post(response);
        IGame game = gameManagement.getGame(request.getLobbyId());
        post(new BoardUpdateEvent(request.getLobbyId(), GameMapper.toDTO(game)));
    }

    /**
     * Handles the DrawPlayerCardRequest event.
     *
     * @param request the request to draw a player card
     */
    @Subscribe
    public void onDiscardPlayerCardRequest(DiscardPlayerCardRequest request) {
        LOG.debug("DiscardPlayerCardRequest received");
        IGame game = gameManagement.getGame(request.getLobbyId());
        Session session = request.getSession()
                                 .orElseThrow(() -> new IllegalStateException(SESSION_NOT_PRESENT));

        try {
            playerManagement.discardPlayerCard(
                    request.getLobbyId(),
                    session.getUser()
                           .getUsername(),
                    request.getCard()
                           .getId()
            );
        } catch (PlayerManagementException e) {
            LOG.error("Error discarding a player card: {}", e.getMessage());
            StatusResponse response = new StatusResponse(
                    request.getLobbyId(),
                    false,
                    "Error discarding a player card: " + e.getMessage()
            );
            response.setSession(session);
            post(response);
        }

        LOG.debug("DiscardPlayerCardRequest processed successfully");
        post(new BoardUpdateEvent(request.getLobbyId(), GameMapper.toDTO(game)));
    }

    /**
     * Handles the DrawInfectionCardRequest event.
     *
     * @param request the request to draw an infection card
     */
    @Subscribe
    public void onDrawInfectionCardRequest(DrawInfectionCardRequest request) {
        AbstractResponseMessage response;
        Session session = request.getSession()
                                 .orElseThrow(() -> new IllegalStateException(SESSION_NOT_PRESENT));
        IGame game = gameManagement.getGame(request.getLobbyId());
        if (!game.getCurrentPlayer()
                 .getUser()
                 .equals(UserMapper.toUser(session.getUser()))) {
            response = new StatusResponse(request.getLobbyId(), false, "It is not your turn");
            response.setSession(session);
            post(response);
            return;
        }
        gameManagement.drawInfectionCard(game);
        post(new BoardUpdateEvent(request.getLobbyId(), GameMapper.toDTO(game)));
    }

    /**
     * Handles the ShareRideRequest event.
     *
     * @param request the request to share a ride
     * @throws PlayerManagementException if there is an error in player management
     */
    @Subscribe
    public void onShareRideRequest(ShareRideRequest request) throws PlayerManagementException {
        if (request.isConfirmed()) {
            Session session = request.getSession()
                                     .orElseThrow(() -> new IllegalStateException(SESSION_NOT_PRESENT));
            playerManagement.setPlayerLocation(
                    request.getLobbyId(),
                    session.getUser()
                           .getUsername(),
                    request.getCityId()
            );
        }
        gameManagement.unlockGameInWaitForConfirmation(request.getLobbyId());

        IGame game = gameManagement.getGame(request.getLobbyId());
        post(new BoardUpdateEvent(request.getLobbyId(), GameMapper.toDTO(game)));
    }

    /**
     * Handles the event when the amount of cards a player has changes.
     *
     * @param lobbyId  the ID of the lobby
     * @param username the username of the player
     * @param cards    the list of card DTOs
     */
    @Override
    public void onCardsAmountChanged(String lobbyId, String username, List<ICardDTO> cards) {
        IGame game = gameManagement.getGame(lobbyId);
        IPlayer player = game.getPlayer(username);
        List<ICard> playerCards = player.getCards();
        if (playerCards.size() > 7) {
            LOG.info("Player {} has exceeded the card limit", username);
            sendDiscardPlayerCardEvent(game.getGameId(), player.getUser(), CardMapper.toMixedCardDTOList(playerCards));
        }
    }

    /**
     * Sends an event to discard a player's cards.
     *
     * @param lobbyId the ID of the lobby
     * @param user    the user whose cards are to be discarded
     * @param cards   the list of card DTOs to be discarded
     */
    public void sendDiscardPlayerCardEvent(String lobbyId, IUser user, List<ICardDTO> cards) {
        DiscardPlayerCardEvent event = new DiscardPlayerCardEvent(lobbyId, cards);
        Session session = authenticationService.getSession(user)
                                               .orElseThrow();
        event.setSession(session);
        bus.post(event);
        LOG.debug("Sent DiscardPlayerCardEvent to user {}", user.getUsername());
    }
}