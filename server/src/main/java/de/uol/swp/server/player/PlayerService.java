package de.uol.swp.server.player;

import com.google.inject.Inject;
import de.uol.swp.common.cards.data.ICardDTO;
import de.uol.swp.common.game.dto.IGameDTO;
import de.uol.swp.common.game.message.event.BoardUpdateEvent;
import de.uol.swp.common.game.message.request.PositioningRequest;
import de.uol.swp.common.game.message.request.ShareRideRequest;
import de.uol.swp.common.game.message.response.StatusResponse;
import de.uol.swp.common.message.response.AbstractResponseMessage;
import de.uol.swp.common.player.message.event.DiscardPlayerCardEvent;
import de.uol.swp.common.player.message.request.DiscardPlayerCardRequest;
import de.uol.swp.common.player.message.request.DrawInfectionCardRequest;
import de.uol.swp.common.player.message.request.DrawPlayerCardRequest;
import de.uol.swp.common.player.message.request.GetCardsToSortRequest;
import de.uol.swp.common.player.message.request.SortedCardsRequest;
import de.uol.swp.common.player.message.response.CardsToSortResponse;
import de.uol.swp.common.player.message.response.DrawPlayerCardResponse;
import de.uol.swp.common.player.message.request.PlacePreventionMarkerRequest;
import de.uol.swp.common.player.message.event.RegionsForPreventionMarkerEvent;
import de.uol.swp.common.region.IRegionDTO;
import de.uol.swp.common.user.Session;
import de.uol.swp.server.AbstractService;
import de.uol.swp.server.city.data.ICity;
import de.uol.swp.server.cards.CardMapper;
import de.uol.swp.server.cards.data.ICard;
import de.uol.swp.server.game.GameMapper;
import de.uol.swp.server.game.data.IGame;
import de.uol.swp.server.game.exceptions.GameException;
import de.uol.swp.server.game.exceptions.IllegalGameStateException;
import de.uol.swp.server.game.management.IGameManagement;
import de.uol.swp.server.game.states.PlacePreventionMarkerState;
import de.uol.swp.server.game.states.PlayerTurnState;
import de.uol.swp.server.game.states.WaitForPositioning;
import de.uol.swp.server.lobby.data.ILobby;
import de.uol.swp.server.lobby.management.ILobbyManagement;
import de.uol.swp.server.player.data.IPlayer;
import de.uol.swp.server.player.data.CardsAmountChangeListener;
import de.uol.swp.server.player.management.IPlayerManagement;
import de.uol.swp.server.player.management.PlayerManagementException;
import de.uol.swp.server.usermanagement.IUser;
import de.uol.swp.server.usermanagement.UserMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import de.uol.swp.server.usermanagement.exceptions.SessionNotFoundException;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Service class for handling player-related operations.
 */
public class PlayerService extends AbstractService implements CardsAmountChangeListener, PositionChangeListener {
    public static final Logger LOG = LogManager.getLogger(PlayerService.class);

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

        playerManagement.setCardsAmountChangeListener(this);

    }

    /**
     * Handles incoming requests to set a players position. This method initializes the position
     * through the GameManagement class, checks if the positioning was successful,
     * and sends an appropriate status response to the requester.
     *
     * @param request the game PositioningRequest containing necessary initialization parameters
     */
    @Subscribe
    public void onPositionRequest(PositioningRequest request) {
        IGame game = playerManagement.getGame(request.getLobbyId());
        Session session = request.getSession()
                                 .orElseThrow(() -> new IllegalStateException("Session not present"));

        try {
            IPlayer player = playerManagement.getPlayer(game, session.getUser().getUsername());
            player.setPositionChangeListener(this);
            gameManagement.setPositioning(request);
        } catch (IllegalStateException | PlayerManagementException e) {
            LOG.error("Could not set positioning for lobby {}", request.getLobbyId());
            sendStatusResponse(request,
                    false,
                    "Position konnte nicht gesetzt werden. Spiel ist in einem ungültigen Zustand"
            );
            return;
        } catch (GameException e) {
            LOG.error("Could not set positioning for lobby {}", request.getLobbyId());
            sendStatusResponse(request, false, "Position konnte nicht gesetzt werden");
            return;
        }
        ILobby lobby = lobbyManagement.getLobby(request.getLobbyId());
        if (game != null && lobby != null) {
            sendToAllInLobby(lobby, new BoardUpdateEvent(request.getLobbyId(), GameMapper.toDTO(game)));
        }
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
                                 .orElseThrow(SessionNotFoundException::new);
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
                                 .orElseThrow(SessionNotFoundException::new);
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
                                     .orElseThrow(SessionNotFoundException::new);
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
     * Handles the DrawPlayerCardRequest event.
     *
     * @param request the request to draw a player card
     */
    @Subscribe
    public void onDiscardPlayerCardRequest(DiscardPlayerCardRequest request) {
        LOG.debug("DiscardPlayerCardRequest received");
        IGame game = playerManagement.getGame(request.getLobbyId());
        Session session = request.getSession()
                                 .orElseThrow(SessionNotFoundException::new);

        try {
            playerManagement.discardPlayerCard(
                    request.getLobbyId(),
                    session.getUser()
                           .getUsername(),
                    request.getCard()
                           .getId()
            );

            LOG.debug("DiscardPlayerCardRequest processed successfully");
            post(new BoardUpdateEvent(request.getLobbyId(), GameMapper.toDTO(game)));
        } catch (GameException e) {
            LOG.error("Error discarding a player card: {}", e.getMessage());
            StatusResponse response = new StatusResponse(
                    request.getLobbyId(),
                    false,
                    "Error discarding a player card: " + e.getMessage()
            );
            response.setSession(session);
            post(response);
        }
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
        IGame game = playerManagement.getGame(lobbyId);
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

    /**
     * Handles the GetCardsToSortRequest event.
     *
     * @param request the request to get cards to sort
     */
    @Subscribe
    public void onGetCardsToSortRequest(GetCardsToSortRequest request) {
        LOG.debug("GetCardsToSortRequest received");
        AbstractResponseMessage response;
        Optional<Session> session = request.getSession();
        try {
            List<ICardDTO> cards = playerManagement.getCardsToSort(
                    request.getLobbyId(),
                    UserMapper.toUser(Objects.requireNonNull(session.map(Session::getUser)
                                                                    .orElse(null)))
            );
            LOG.debug("GetCardsToSortRequest processed successfully");
            response = new CardsToSortResponse(request.getLobbyId(), true, "Karten wurden erfolgreich ermittelt", cards);
        } catch (GameException | IllegalGameStateException e) {
            LOG.error("Error retrieving cards: {}", e.getMessage());
            response = new StatusResponse(request.getLobbyId(), false, "Es ist nicht dein Zug oder du bist kein Wissenschaftler an der Königlichen Akademie");
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
    public void onSortedCardsRequest(SortedCardsRequest request) throws GameException {
        LOG.debug("SortedCardsRequest received");
        IGame game = playerManagement.getGame(request.getLobbyId());
        Optional<Session> session = request.getSession();
        try {
            playerManagement.sortCards(
                    request.getLobbyId(),
                    UserMapper.toUser(Objects.requireNonNull(session.map(Session::getUser)
                                                                    .orElse(null))),
                    request.getCards()
            );
            IGameDTO gameDTO = GameMapper.toDTO(game);
            ILobby lobby = lobbyManagement.getLobby(request.getLobbyId());
            LOG.debug("SortedCardsRequest processed successfully");
            sendToAllInLobby(lobby, new BoardUpdateEvent(request.getLobbyId(), gameDTO));
            sendStatusResponse(request, true, "Karten wurden erfolgreich sortiert");
            sendServerMessageEvent(request.getLobbyId(), "Die Wissenschaftlerin der königlichen Akademie hat die Karten auf dem Nachziehstapel sortiert");
        } catch (IllegalStateException e) {
            LOG.error("Error sorting cards: {}", e.getMessage());
            sendStatusResponse(request, false, "Es ist nicht dein Zug oder du bist kein Wissenschaftler an der Königlichen Akademie");
        }
    }

    /**
     * Handles the PlacePreventionMarkerRequest event.
     *
     * @param request the request to place a prevention marker
     */
    @Subscribe
    public void onPlacePreventionMarkerRequest(PlacePreventionMarkerRequest request) {
        LOG.debug("PlacePreventionMarkerRequest received");
        IGame game = playerManagement.getGame(request.getLobbyId());
        ILobby lobby = lobbyManagement.getLobby(request.getLobbyId());

        playerManagement.placePreventionMarker(request.getLobbyId(), request.getRegionId());
        if(game.getPreviousState() instanceof WaitForPositioning waitForPositioningState && waitForPositioningState.getPositionedPlayersCount() == game.getPlayers().size()) {
            game.setState(new PlayerTurnState());
        } else {
            game.setState(game.getPreviousState());
        }
        LOG.debug("PlacePreventionMarkerRequest processed successfully");
        sendServerMessageEvent(request.getLobbyId(), "Präventionsmarker wurde erfolgreich platziert");
        sendStatusResponse(request, true, "Präventionsmarker wurde erfolgreich platziert");
        sendToAllInLobby(lobby, new BoardUpdateEvent(request.getLobbyId(), GameMapper.toDTO(game)));
    }

    /**
     * Handles the event when a player's position changes.
     *
     * @param player      the player whose position has changed
     * @param oldPosition the old position of the player
     * @param newPosition the new position of the player
     */
    @Override
    public void onPositionChanged(IPlayer player, ICity oldPosition, ICity newPosition) {
        LOG.debug("Nurse position change received");
        Session session = authenticationService.getSession(player.getUser())
                                               .orElseThrow(() -> new IllegalStateException("Session not present"));
        List<IRegionDTO> regions = playerManagement.determineRegionsForNurse(player, oldPosition, newPosition);
        IGame game = playerManagement.getGame(player.getGameId());
        game.setState(new PlacePreventionMarkerState());
        LOG.debug("Regions for nurse determined successfully");
        RegionsForPreventionMarkerEvent event = new RegionsForPreventionMarkerEvent(
                player.getGameId(),
                regions
        );
        event.setReceiver(List.of(session));
        post(event);
    }
}