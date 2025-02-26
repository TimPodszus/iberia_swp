package de.uol.swp.server.player;

import com.google.inject.Inject;
import de.uol.swp.common.cards.data.ICardDTO;
import de.uol.swp.common.game.dto.IGameDTO;
import de.uol.swp.common.game.message.event.BoardUpdateEvent;
import de.uol.swp.common.game.message.request.ShareRideRequest;
import de.uol.swp.common.game.message.response.StatusResponse;
import de.uol.swp.common.message.response.AbstractResponseMessage;
import de.uol.swp.common.player.request.DrawInfectionCardRequest;
import de.uol.swp.common.player.request.DrawPlayerCardRequest;
import de.uol.swp.common.player.request.GetCardsToSortRequest;
import de.uol.swp.common.player.request.SortedCardsRequest;
import de.uol.swp.common.player.response.CardsToSortResponse;
import de.uol.swp.common.player.response.DrawPlayerCardResponse;
import de.uol.swp.common.user.Session;
import de.uol.swp.server.AbstractService;
import de.uol.swp.server.game.GameMapper;
import de.uol.swp.server.game.data.IGame;
import de.uol.swp.server.game.management.IGameManagement;
import de.uol.swp.server.lobby.data.ILobby;
import de.uol.swp.server.lobby.management.ILobbyManagement;
import de.uol.swp.server.player.management.IPlayerManagement;
import de.uol.swp.server.player.management.PlayerManagementException;
import de.uol.swp.server.usermanagement.UserMapper;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Service class for handling player-related operations.
 */
public class PlayerService extends AbstractService {
    IPlayerManagement playerManagement;
    IGameManagement gameManagement;
    ILobbyManagement lobbyManagement;

    /**
     * Constructs a new PlayerService.
     *
     * @param bus              the EventBus instance for event handling
     * @param playerManagement the player management instance for player operations
     */
    @Inject
    public PlayerService(EventBus bus, IPlayerManagement playerManagement, IGameManagement gameManagement,
                         ILobbyManagement lobbyManagement
    ) {
        super(bus);
        this.playerManagement = playerManagement;
        this.gameManagement = gameManagement;
        this.lobbyManagement = lobbyManagement;
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
                                 .orElseThrow(() -> new IllegalStateException("Session not present"));
        try {
            ICardDTO card = playerManagement.drawPlayerCard(request.getLobbyId(), UserMapper.toUser(session.getUser()));
            response = new DrawPlayerCardResponse(request.getLobbyId(), true, "Card drawn successfully", card);
        } catch (PlayerManagementException e) {
            response = new StatusResponse(request.getLobbyId(), false, "Error drawing a player card");
        }
        response.setSession(session);
        post(response);
        IGame game = playerManagement.getGame(request.getLobbyId());
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
                                 .orElseThrow(() -> new IllegalStateException("Session not present"));
        IGame game = playerManagement.getGame(request.getLobbyId());
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
                                     .orElseThrow(() -> new IllegalStateException("Session not present"));
            playerManagement.setPlayerLocation(
                    request.getLobbyId(),
                    session.getUser()
                           .getUsername(),
                    request.getCityId()
            );
        }
        gameManagement.unlockGameInWaitForConfirmation(request.getLobbyId());

        IGame game = playerManagement.getGame(request.getLobbyId());
        post(new BoardUpdateEvent(request.getLobbyId(), GameMapper.toDTO(game)));
    }

    /**
     * Handles the GetCardsToSortRequest event.
     *
     * @param request the request to get cards to sort
     */
    @Subscribe
    public void onGetCardsToSortRequest(GetCardsToSortRequest request) {
        AbstractResponseMessage response;
        Optional<Session> session = request.getSession();
        try {
            List<ICardDTO> cards = playerManagement.getCardsToSort(request.getLobbyId(),
                    UserMapper.toUser(Objects.requireNonNull(session.map(Session::getUser)
                                                                    .orElse(null))));
            response = new CardsToSortResponse(request.getLobbyId(), true, "Cards retrieved successfully", cards);
        } catch (PlayerManagementException | IllegalStateException e) {
            response = new StatusResponse(request.getLobbyId(), false, e.getMessage());
        }
        response.setSession(session.orElse(null));
        post(response);
    }

    /**
     * Handles the SortedCardsRequest event.
     *
     * @param request the request to sort cards
     */
    @Subscribe
    public void onSortedCardsRequest(SortedCardsRequest request) {
        AbstractResponseMessage response;
        Optional<Session> session = request.getSession();
        IGame game = playerManagement.getGame(request.getLobbyId());
        try {
            playerManagement.sortCards(
                    request.getLobbyId(),
                    UserMapper.toUser(Objects.requireNonNull(session.map(Session::getUser)
                                                                    .orElse(null))),
                    request.getCards()
            );
            response = new StatusResponse(request.getLobbyId(), true, "Karten wurden erfolgreich sortiert");
        } catch (IllegalStateException e) {
            response = new StatusResponse(request.getLobbyId(), false, e.getMessage());
        }
        response.setSession(session.orElse(null));
        post(response);

        IGameDTO gameDTO = GameMapper.toDTO(game);
        ILobby lobby = lobbyManagement.getLobby(request.getLobbyId());
        sendToAllInLobby(lobby, new BoardUpdateEvent(request.getLobbyId(), gameDTO));
    }
}