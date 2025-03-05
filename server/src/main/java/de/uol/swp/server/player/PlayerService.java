package de.uol.swp.server.player;

import com.google.inject.Inject;
import de.uol.swp.common.cards.data.ICardDTO;
import de.uol.swp.common.game.message.event.BoardUpdateEvent;
import de.uol.swp.common.game.message.request.PositioningRequest;
import de.uol.swp.common.game.message.request.ShareRideRequest;
import de.uol.swp.common.game.message.response.StatusResponse;
import de.uol.swp.common.message.response.AbstractResponseMessage;
import de.uol.swp.common.player.message.request.DrawInfectionCardRequest;
import de.uol.swp.common.player.message.request.DrawPlayerCardRequest;
import de.uol.swp.common.player.message.request.DrawPlayerCardResponse;
import de.uol.swp.common.player.message.request.PlacePreventionMarkerRequest;
import de.uol.swp.common.player.message.response.RegionsForPreventionMarkerResponse;
import de.uol.swp.common.region.IRegionDTO;
import de.uol.swp.common.user.Session;
import de.uol.swp.server.AbstractService;
import de.uol.swp.server.city.data.ICity;
import de.uol.swp.server.game.GameMapper;
import de.uol.swp.server.game.data.IGame;
import de.uol.swp.server.game.exceptions.GameException;
import de.uol.swp.server.game.management.IGameManagement;
import de.uol.swp.server.game.states.PlacePreventionMarkerState;
import de.uol.swp.server.lobby.data.ILobby;
import de.uol.swp.server.lobby.management.ILobbyManagement;
import de.uol.swp.server.player.data.IPlayer;
import de.uol.swp.server.player.management.IPlayerManagement;
import de.uol.swp.server.player.management.PlayerManagementException;
import de.uol.swp.server.usermanagement.UserMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

/**
 * Service class for handling player-related operations.
 */
public class PlayerService extends AbstractService implements PositionChangeListener {
    static final Logger LOG = LogManager.getLogger(PlayerService.class);

    private final IPlayerManagement playerManagement;
    private final IGameManagement gameManagement;
    private final ILobbyManagement lobbyManagement;


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
     * Handles incoming requests to set a players position. This method initializes the position
     * through the GameManagement class, checks if the positioning was successful,
     * and sends an appropriate status response to the requester.
     *
     * @param request the game PositioningRequest containing necessary initialization parameters
     */
    @Subscribe
    public void onPositionRequest(PositioningRequest request) {
        IGame game = gameManagement.getGame(request.getLobbyId());
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
                                 .orElseThrow(() -> new IllegalStateException("Session not present"));
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
     * Handles the DrawInfectionCardRequest event.
     *
     * @param request the request to draw an infection card
     */
    @Subscribe
    public void onDrawInfectionCardRequest(DrawInfectionCardRequest request) {
        AbstractResponseMessage response;
        Session session = request.getSession()
                                 .orElseThrow(() -> new IllegalStateException("Session not present"));
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
                                     .orElseThrow(() -> new IllegalStateException("Session not present"));
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

    @Subscribe
    public void onPlacePreventionMarkerRequest(PlacePreventionMarkerRequest request) {
        playerManagement.placePreventionMarker(request.getLobbyId(), request.getRegionId());
        IGame game = gameManagement.getGame(request.getLobbyId());
        game.setState(game.getPreviousState());
        ILobby lobby = lobbyManagement.getLobby(request.getLobbyId());
        sendToAllInLobby(lobby, new BoardUpdateEvent(request.getLobbyId(), GameMapper.toDTO(game)));
    }

    @Override
    public void onPositionChanged(IPlayer player, ICity oldPosition, ICity newPosition) {
        Session session = authenticationService.getSession(player.getUser())
                                               .orElseThrow(() -> new IllegalStateException("Session not present"));
        List<IRegionDTO> regions = playerManagement.determineRegionsForNurse(player, oldPosition, newPosition);
        IGame game = gameManagement.getGame(player.getGameId());
        game.setState(new PlacePreventionMarkerState());
        RegionsForPreventionMarkerResponse response = new RegionsForPreventionMarkerResponse(
                player.getGameId(),
                true,
                regions
        );
        response.setSession(session);
        post(response);
    }
}
